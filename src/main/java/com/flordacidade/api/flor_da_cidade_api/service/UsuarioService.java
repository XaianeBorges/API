package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.model.Pessoa;
import com.flordacidade.api.flor_da_cidade_api.model.Usuario;
import com.flordacidade.api.flor_da_cidade_api.repository.PessoaRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final PessoaRepository pessoaRepo;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepo,
                          PessoaRepository pessoaRepo) {
        this.usuarioRepo = usuarioRepo;
        this.pessoaRepo = pessoaRepo;
    }

    public List<Usuario> getAll() {
        return usuarioRepo.findAll();
    }

    public Optional<Usuario> getById(Integer id) {
        return usuarioRepo.findById(id);
    }

    @Transactional
    public Usuario createFromDTO(UsuarioDTO dto) {
        Usuario u = dtoToEntity(dto);
        return usuarioRepo.save(u);
    }

    @Transactional
    public Usuario updateFromDTO(Integer id, UsuarioDTO dto) {
        return usuarioRepo.findById(id)
                .map(existing -> {
                    Usuario updated = dtoToEntity(dto);
                    updated.setId(existing.getId());
                    return usuarioRepo.save(updated);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
    }

    @Transactional
    public void delete(Integer id) {
        usuarioRepo.deleteById(id);
    }

    // --- Converte UsuarioDTO -> Usuario (persistência) ---
    private Usuario dtoToEntity(UsuarioDTO dto) {
        Pessoa pessoa = pessoaRepo.findById(dto.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada: " + dto.getPessoaId()));

        Usuario u = new Usuario();
        u.setPessoa(pessoa);
        u.setSenha(dto.getSenha());
        u.setAtivo(dto.getAtivo());
        u.setMatricula(dto.getMatricula());
        return u;
    }

    // --- Converte Usuario -> UsuarioResponseDTO (resposta JSON) ---
    public UsuarioResponseDTO toResponseDTO(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setPessoaId(u.getPessoa().getId());
        dto.setSenha(u.getSenha());
        dto.setDataCriacao(u.getDataCriacao());
        dto.setDataAtualizacao(u.getDataAtualizacao());
        dto.setAtivo(u.getAtivo());
        dto.setMatricula(u.getMatricula());
        return dto;
    }
}
