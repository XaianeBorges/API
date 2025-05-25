package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.PessoaDTO;
import com.flordacidade.api.flor_da_cidade_api.model.Escolaridade;
import com.flordacidade.api.flor_da_cidade_api.model.Pessoa;
import com.flordacidade.api.flor_da_cidade_api.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {
    private final PessoaRepository repository;

    @Autowired
    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    public List<Pessoa> getAll() {
        return repository.findAll();
    }

    public Optional<Pessoa> getById(Integer id) {
        return repository.findById(id);
    }

    public Pessoa createFromDTO(PessoaDTO dto) {
        Pessoa pessoa = dtoToEntity(dto);
        return repository.save(pessoa);
    }

    public Pessoa updateFromDTO(Integer id, PessoaDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    Pessoa toSave = dtoToEntity(dto);
                    toSave.setId(existing.getId());
                    return repository.save(toSave);
                })
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com id " + id));
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    private Pessoa dtoToEntity(PessoaDTO dto) {
        Pessoa p = new Pessoa();
        p.setNome(dto.getNome());
        p.setCpf(dto.getCpf());
        p.setDataNascimento(dto.getDataNascimento());
        p.setEmail(dto.getEmail());
        p.setEndereco(dto.getEndereco());
        p.setTelefone(dto.getTelefone());
        if (dto.getEscolaridade() != null) {
            p.setEscolaridade(Escolaridade.fromLabel(dto.getEscolaridade()));
        }
        return p;
    }
}