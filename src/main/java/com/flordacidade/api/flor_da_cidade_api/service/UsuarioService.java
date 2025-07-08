// src/main/java/com/flordacidade/api/flor_da_cidade_api/service/UsuarioService.java
package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.PessoaModel;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import com.flordacidade.api.flor_da_cidade_api.repository.PessoaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flordacidade.api.flor_da_cidade_api.exception.BusinessException;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioModel> getAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioModel> getById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public UsuarioModel create(UsuarioModel usuario) {
        PessoaModel pessoa = pessoaRepository.findById(usuario.getPessoa().getIdPessoa())
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));
        usuario.setPessoa(pessoa);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Optional<UsuarioModel> update(Integer id, UsuarioModel usuario) {
        return usuarioRepository.findById(id)
                .map(existing -> {
                    PessoaModel pessoa = pessoaRepository.findById(usuario.getPessoa().getIdPessoa())
                            .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));
                    existing.setPessoa(pessoa);
                    existing.setAtivo(usuario.getAtivo());
                    existing.setMatricula(usuario.getMatricula());

                    if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
                        existing.setSenha(passwordEncoder.encode(usuario.getSenha()));
                    }
                    return usuarioRepository.save(existing);
                });
    }

    @Transactional
    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public void solicitarRedefinicaoSenha(String email) {
        // 1. Encontra a pessoa pelo e-mail
        PessoaModel pessoa = pessoaRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("E-mail não encontrado no sistema."));

        // 2. Encontra o usuário associado à pessoa
        UsuarioModel usuario = usuarioRepository.findByPessoa(pessoa)
                .orElseThrow(() -> new BusinessException("Usuário não associado a este e-mail."));

        // 3. Gera e salva o token
        String token = UUID.randomUUID().toString();
        usuario.setResetPasswordToken(token);
        usuario.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1)); // Token expira em 1 hora
        usuarioRepository.save(usuario);

        // 4. Envia o e-mail
        emailService.sendPasswordResetEmail(pessoa.getEmail(), token);
    }

    @Transactional
    public void redefinirSenha(String token, String novaSenha) {
        if (novaSenha == null || novaSenha.length() < 8) {
            throw new BusinessException("A nova senha deve ter no mínimo 8 caracteres.");
        }

        // 1. Encontra o usuário pelo token
        UsuarioModel usuario = usuarioRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new BusinessException("Token inválido ou expirado."));

        // 2. Verifica se o token expirou
        if (usuario.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            // Invalida o token expirado
            usuario.setResetPasswordToken(null);
            usuario.setResetPasswordTokenExpiry(null);
            usuarioRepository.save(usuario);
            throw new BusinessException("Token expirado. Por favor, solicite uma nova redefinição de senha.");
        }

        // 3. Redefine a senha (criptografada) e invalida o token
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordTokenExpiry(null);

        usuarioRepository.save(usuario);
    }
}
