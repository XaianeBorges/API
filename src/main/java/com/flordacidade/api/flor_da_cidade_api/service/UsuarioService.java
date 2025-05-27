// src/main/java/com/flordacidade/api/flor_da_cidade_api/service/UsuarioService.java
package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.PessoaModel;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import com.flordacidade.api.flor_da_cidade_api.repository.PessoaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final PessoaRepository pessoaRepo;

    @Transactional(readOnly = true)
    public List<UsuarioModel> getAll() {
        return usuarioRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioModel> getById(Integer id) {
        return usuarioRepo.findById(id);
    }

    @Transactional
    public UsuarioModel create(UsuarioModel usuario) {
        PessoaModel pessoa = pessoaRepo.findById(usuario.getPessoa().getIdPessoa())
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));
        usuario.setPessoa(pessoa);
        return usuarioRepo.save(usuario);
    }

    @Transactional
    public Optional<UsuarioModel> update(Integer id, UsuarioModel usuario) {
        return usuarioRepo.findById(id)
                .map(existing -> {
                    PessoaModel pessoa = pessoaRepo.findById(usuario.getPessoa().getIdPessoa())
                            .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));
                    existing.setPessoa(pessoa);
                    existing.setSenha(usuario.getSenha());
                    existing.setAtivo(usuario.getAtivo());
                    existing.setMatricula(usuario.getMatricula());
                    return usuarioRepo.save(existing);
                });
    }

    @Transactional
    public void delete(Integer id) {
        usuarioRepo.deleteById(id);
    }
}