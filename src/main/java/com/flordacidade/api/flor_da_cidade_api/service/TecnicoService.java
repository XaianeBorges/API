package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoCreateDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.TecnicoMapper;
import com.flordacidade.api.flor_da_cidade_api.exception.BusinessException;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.model.RegiaoModel;
import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.RegiaoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.TecnicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final RegiaoRepository regiaoRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TecnicoMapper tecnicoMapper;

    @Autowired
    public TecnicoService(TecnicoRepository tecnicoRepository, RegiaoRepository regiaoRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder, TecnicoMapper tecnicoMapper) {
        this.tecnicoRepository = tecnicoRepository;
        this.regiaoRepository = regiaoRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.tecnicoMapper = tecnicoMapper;
    }

    @Transactional(readOnly = true)
    public List<TecnicoModel> getAll() {
        return tecnicoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TecnicoModel> getById(Integer id) {
        return tecnicoRepository.findById(id);
    }

    @Transactional
    public TecnicoModel create(TecnicoCreateDTO tecnicoDTO) {
        if (tecnicoRepository.findByMatricula(tecnicoDTO.getMatricula()).isPresent()) {
            throw new BusinessException("Matrícula já cadastrada: " + tecnicoDTO.getMatricula());
        }
        if (tecnicoRepository.findByEmail(tecnicoDTO.getEmail()).isPresent()) {
            throw new BusinessException("E-mail já cadastrado: " + tecnicoDTO.getEmail());
        }

        TecnicoModel novoTecnico = tecnicoMapper.createDtoToEntity(tecnicoDTO);

        RegiaoModel regiao = regiaoRepository.findById(tecnicoDTO.getIdRegiao())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Região inválida com ID: " + tecnicoDTO.getIdRegiao()));
        novoTecnico.setRegiao(regiao);

        novoTecnico.setSenha(passwordEncoder.encode(tecnicoDTO.getSenha()));

        return tecnicoRepository.save(novoTecnico);
    }

    @Transactional
    public TecnicoModel update(Integer id, TecnicoUpdateDTO tecnicoDTO) {
        TecnicoModel existingTecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado com ID: " + id));

        // Atualiza campos simples
        tecnicoMapper.updateEntityFromDto(tecnicoDTO, existingTecnico);

        // Atualiza senha (se fornecida)
        if (tecnicoDTO.getSenha() != null && !tecnicoDTO.getSenha().isBlank()) {
            existingTecnico.setSenha(passwordEncoder.encode(tecnicoDTO.getSenha()));
        }

        // Atualiza região (se fornecida)
        if (tecnicoDTO.getIdRegiao() != null) {
            RegiaoModel regiao = regiaoRepository.findById(tecnicoDTO.getIdRegiao())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Região inválida com ID: " + tecnicoDTO.getIdRegiao()));
            existingTecnico.setRegiao(regiao);
        }

        return tecnicoRepository.save(existingTecnico);
    }

    @Transactional
    public void delete(Integer id) {
        if (!tecnicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Técnico não encontrado com o id: " + id);
        }
        tecnicoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<TecnicoModel> authenticate(String matricula, String senha) {
        Optional<TecnicoModel> tecnicoOpt = tecnicoRepository.findByMatriculaAndSenha(matricula, senha);
        if (tecnicoOpt.isPresent() && tecnicoOpt.get().getStatus() == TecnicoModel.TecnicoStatus.ATIVO) {
            return tecnicoOpt;
        }
        return Optional.empty();
    }

    @Transactional
    public void solicitarRedefinicaoSenha(String email) {
        TecnicoModel tecnico = tecnicoRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("E-mail não encontrado no sistema."));

        String token = UUID.randomUUID().toString();
        tecnico.setResetPasswordToken(token);
        tecnico.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1)); // Token expira em 1 hora
        tecnicoRepository.save(tecnico);

        emailService.sendPasswordResetEmail(tecnico.getEmail(), token);
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha) {
        if (novaSenha == null || novaSenha.length() < 8) {
            throw new BusinessException("A nova senha deve ter no mínimo 8 caracteres.");
        }

        TecnicoModel tecnico = tecnicoRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new BusinessException("Token inválido ou expirado."));

        if (tecnico.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            tecnico.setResetPasswordToken(null);
            tecnico.setResetPasswordTokenExpiry(null);
            tecnicoRepository.save(tecnico);
            throw new BusinessException("Token expirado. Por favor, solicite uma nova redefinição de senha.");
        }

        tecnico.setSenha(passwordEncoder.encode(novaSenha));
        tecnico.setResetPasswordToken(null);
        tecnico.setResetPasswordTokenExpiry(null);

        tecnicoRepository.save(tecnico);
    }
}