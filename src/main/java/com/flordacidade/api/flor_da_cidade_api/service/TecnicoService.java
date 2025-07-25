package com.flordacidade.api.flor_da_cidade_api.service;

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

    @Autowired
    public TecnicoService(TecnicoRepository tecnicoRepository, RegiaoRepository regiaoRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder) {
        this.tecnicoRepository = tecnicoRepository;
        this.regiaoRepository = regiaoRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;

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
    public TecnicoModel create(TecnicoModel tecnico) {
        if (tecnicoRepository.findByMatricula(tecnico.getMatricula()).isPresent()) {
            throw new IllegalArgumentException("Matrícula já cadastrada: " + tecnico.getMatricula());
        }

        if (tecnico.getRegiao() == null || tecnico.getRegiao().getIdRegiao() == null) {
            throw new IllegalArgumentException("ID da Região do técnico não pode ser nulo.");
        }
        RegiaoModel regiao = regiaoRepository.findById(tecnico.getRegiao().getIdRegiao())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Região inválida com ID: " + tecnico.getRegiao().getIdRegiao()));
        tecnico.setRegiao(regiao);

        if (tecnico.getSenha() == null || tecnico.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser vazia.");
        }

        // LOG PARA DEPURAR O VALOR DE isAdm RECEBIDO
        System.out.println("TecnicoService DEBUG: Recebido para criar - Nome: " + tecnico.getNome() + ", Matrícula: "
                + tecnico.getMatricula() + ", isAdm: " + tecnico.isAdm());

        return tecnicoRepository.save(tecnico);
    }

    @Transactional
    public Optional<TecnicoModel> update(Integer id, TecnicoModel tecnicoDetails) {
        return tecnicoRepository.findById(id)
                .map(existingTecnico -> {
                    existingTecnico.setNome(tecnicoDetails.getNome());

                    if (!existingTecnico.getMatricula().equals(tecnicoDetails.getMatricula())) {
                        if (tecnicoRepository.findByMatricula(tecnicoDetails.getMatricula())
                                .filter(t -> !t.getIdTecnico().equals(id)).isPresent()) {
                            throw new IllegalArgumentException("Nova matrícula '" + tecnicoDetails.getMatricula()
                                    + "' já cadastrada para outro técnico.");
                        }
                        existingTecnico.setMatricula(tecnicoDetails.getMatricula());
                    }

                    if (tecnicoDetails.getSenha() != null && !tecnicoDetails.getSenha().trim().isEmpty()) {
                        existingTecnico.setSenha(tecnicoDetails.getSenha());
                    }

                    existingTecnico.setStatus(tecnicoDetails.getStatus());

                    if (tecnicoDetails.getRegiao() != null && tecnicoDetails.getRegiao().getIdRegiao() != null) {
                        RegiaoModel regiao = regiaoRepository.findById(tecnicoDetails.getRegiao().getIdRegiao())
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Região inválida com ID: " + tecnicoDetails.getRegiao().getIdRegiao()));
                        existingTecnico.setRegiao(regiao);
                    } else if (tecnicoDetails.getRegiao() != null && tecnicoDetails.getRegiao().getIdRegiao() == null) {
                        throw new IllegalArgumentException("ID da Região não fornecido para atualização.");
                    }

                    // LOG PARA DEPURAR O VALOR DE isAdm NO UPDATE
                    System.out.println("TecnicoService DEBUG: Recebido para atualizar ID " + id + " - isAdm: "
                            + tecnicoDetails.isAdm());
                    existingTecnico.setAdm(tecnicoDetails.isAdm());

                    return tecnicoRepository.save(existingTecnico);
                });
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
        // 1. Encontra a pessoa pelo e-mail
        TecnicoModel tecnico = tecnicoRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("E-mail não encontrado no sistema."));

        // 3. Gera e salva o token
        String token = UUID.randomUUID().toString();
        tecnico.setResetPasswordToken(token);
        tecnico.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1)); // Token expira em 1 hora
        tecnicoRepository.save(tecnico);

        // 4. Envia o e-mail
        emailService.sendPasswordResetEmail(tecnico.getEmail(), token);
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha) {
        if (novaSenha == null || novaSenha.length() < 8) {
            throw new BusinessException("A nova senha deve ter no mínimo 8 caracteres.");
        }

        // 1. Encontra o usuário pelo token
        TecnicoModel tecnico = tecnicoRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new BusinessException("Token inválido ou expirado."));

        // 2. Verifica se o token expirou
        if (tecnico.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            // Invalida o token expirado
            tecnico.setResetPasswordToken(null);
            tecnico.setResetPasswordTokenExpiry(null);
            tecnicoRepository.save(tecnico);
            throw new BusinessException("Token expirado. Por favor, solicite uma nova redefinição de senha.");
        }

        // 3. Redefine a senha (criptografada) e invalida o token
        tecnico.setSenha(passwordEncoder.encode(novaSenha));
        tecnico.setResetPasswordToken(null);
        tecnico.setResetPasswordTokenExpiry(null);

        tecnicoRepository.save(tecnico);
    }
}