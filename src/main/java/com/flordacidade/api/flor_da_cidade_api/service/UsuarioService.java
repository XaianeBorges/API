// src/main/java/com/flordacidade/api/flor_da_cidade_api/service/UsuarioService.java
package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioCreateDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.exception.BusinessException;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.mapper.UsuarioMapper;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import com.flordacidade.api.flor_da_cidade_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public List<UsuarioModel> getAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioModel> getById(Integer id) {
        return usuarioRepository.findById(id);
    }

    private String limparNumeros(String valor) {
        if (valor == null) {
            return null;
        }
        return valor.replaceAll("[^0-9]", ""); // Remove tudo que não for um dígito
    }

    @Transactional
    public UsuarioModel criar(UsuarioCreateDTO usuarioDTO) {
        UsuarioModel novoUsuario = usuarioMapper.createDtoToEntity(usuarioDTO);

        novoUsuario.setCpf(limparNumeros(usuarioDTO.getCpf()));
        novoUsuario.setTelefone(limparNumeros(usuarioDTO.getTelefone()));

        if (usuarioRepository.existsByCpf(novoUsuario.getCpf())) {
            throw new BusinessException("CPF já cadastrado");
        }
        if (usuarioRepository.existsByEmail(novoUsuario.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (usuarioRepository.existsByTelefone(novoUsuario.getTelefone())) {
            throw new BusinessException("Telefone já cadastrado");
        }

        return usuarioRepository.save(novoUsuario);
    }

    @Transactional
    public UsuarioModel atualizar(Integer id, UsuarioUpdateDTO usuarioDTO) {

        UsuarioModel existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        usuarioMapper.updateEntityFromDto(usuarioDTO, existingUsuario);

        if (usuarioDTO.getCpf() != null) {
            existingUsuario.setCpf(limparNumeros(usuarioDTO.getCpf()));
        }
        if (usuarioDTO.getTelefone() != null) {
            existingUsuario.setTelefone(limparNumeros(usuarioDTO.getTelefone()));
        }

        if (usuarioRepository.existsByCpfAndIdUsuarioNot(existingUsuario.getCpf(), id)) {
            throw new BusinessException("CPF já cadastrado em outro usuário");
        }
        if (usuarioRepository.existsByEmailAndIdUsuarioNot(existingUsuario.getEmail(), id)) {
            throw new BusinessException("E-mail já cadastrado em outro usuário");
        }
        if (usuarioRepository.existsByTelefoneAndIdUsuarioNot(existingUsuario.getTelefone(), id)) {
            throw new BusinessException("Telefone já cadastrado em outro usuário");
        }

        return usuarioRepository.save(existingUsuario);
    }

    @Transactional
    public void delete(Integer id) {
        if (!usuarioRepository.existsById(id)) {
        throw new ResourceNotFoundException("Usuário não encontrado com ID: " + id);
    }
        usuarioRepository.deleteById(id);
    }

}
