// src/main/java/com/flordacidade/api/flor_da_cidade_api/service/TecnicoService.java
package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.model.Tecnico;
import com.flordacidade.api.flor_da_cidade_api.repository.TecnicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TecnicoService {

    private final TecnicoRepository repo;

    public TecnicoService(TecnicoRepository repo) {
        this.repo = repo;
    }

    public List<Tecnico> getAll() {
        return repo.findAll();
    }

    public Optional<Tecnico> getById(Integer id) {
        return repo.findById(id);
    }

    @Transactional
    public Tecnico createFromDTO(TecnicoDTO dto) {
        Tecnico t = dtoToEntity(dto);
        return repo.save(t);
    }

    @Transactional
    public Tecnico updateFromDTO(Integer id, TecnicoDTO dto) {
        return repo.findById(id)
                .map(existing -> {
                    Tecnico t = dtoToEntity(dto);
                    t.setId(existing.getId());
                    return repo.save(t);
                })
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado: " + id));
    }

    @Transactional
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    private Tecnico dtoToEntity(TecnicoDTO dto) {
        Tecnico t = new Tecnico();
        t.setStatus(dto.getStatus());
        t.setMatricula(dto.getMatricula());
        t.setSenha(dto.getSenha());
        t.setRegiao(dto.getRegiao());
        t.setNome(dto.getNome());  // ← Mapeamento do nome
        return t;
    }

    public TecnicoResponseDTO toResponseDTO(Tecnico t) {
        TecnicoResponseDTO dto = new TecnicoResponseDTO();
        dto.setId(t.getId());
        dto.setStatus(t.getStatus().name());
        dto.setMatricula(t.getMatricula());
        dto.setDataCriacao(t.getDataCriacao());
        dto.setDataAtualizacao(t.getDataAtualizacao());
        dto.setRegiao(t.getRegiao());
        dto.setNome(t.getNome());  // ← Inclusão do nome na resposta
        return dto;
    }
}