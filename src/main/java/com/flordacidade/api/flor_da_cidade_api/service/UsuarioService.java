// src/main/java/com/flordacidade/api/flor_da_cidade_api/service/UsuarioService.java
package com.flordacidade.api.flor_da_cidade_api.service;

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

    // Colocar o restodoa campos
    @Transactional
    public UsuarioModel criar(UsuarioModel usuario) {
        // Limpa os dados antes da validação de unicidade
        String cpfLimpo = limparNumeros(usuario.getCpf());
        String telefoneLimpo = limparNumeros(usuario.getTelefone());

        usuario.setCpf(cpfLimpo);
        usuario.setTelefone(telefoneLimpo);

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        if (usuarioRepository.existsByTelefone(usuario.getTelefone())) {
            throw new IllegalArgumentException("Telefone já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioModel atualizar(Integer id, UsuarioModel usuarioAtual) {

        String cpfLimpo = limparNumeros(usuarioAtual.getCpf());
        String telefoneLimpo = limparNumeros(usuarioAtual.getTelefone());

        usuarioAtual.setCpf(cpfLimpo);
        usuarioAtual.setTelefone(telefoneLimpo);

        return usuarioRepository.findById(id)
                .map(existing -> {
                    if (usuarioRepository.existsByCpfAndIdUsuarioNot(usuarioAtual.getCpf(), id)) {
                        throw new IllegalArgumentException("CPF já cadastrado");
                    }
                    if (usuarioRepository.existsByEmailAndIdUsuarioNot(usuarioAtual.getEmail(), id)) {
                        throw new IllegalArgumentException("E-mail já cadastrado");
                    }
                    if (usuarioRepository.existsByTelefoneAndIdUsuarioNot(usuarioAtual.getTelefone(), id)) {
                        throw new IllegalArgumentException("Telefone já cadastrado");
                    }
                    // checar se todos os gets estao aqui
                    existing.setNome(usuarioAtual.getNome());
                    existing.setCpf(usuarioAtual.getCpf());
                    existing.setDataNascimento(usuarioAtual.getDataNascimento());
                    existing.setEmail(usuarioAtual.getEmail());
                    existing.setEndereco(usuarioAtual.getEndereco());
                    existing.setTelefone(usuarioAtual.getTelefone());
                    existing.setEscolaridade(usuarioAtual.getEscolaridade());
                    existing.setAtivo(usuarioAtual.getAtivo());
                    return usuarioRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada com ID: " + id));
    }

    @Transactional
    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

}
