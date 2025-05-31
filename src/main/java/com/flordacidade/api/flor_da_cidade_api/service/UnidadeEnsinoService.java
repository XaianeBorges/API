package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.UnidadeEnsino;
import com.flordacidade.api.flor_da_cidade_api.repository.UnidadeEnsinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadeEnsinoService {
    @Autowired
    private UnidadeEnsinoRepository repo;

    public List<UnidadeEnsino> listar() {
        return repo.findAll();
    }

    public Optional<UnidadeEnsino> buscarPorId(Integer id) {
        return repo.findById(id);
    }

    public UnidadeEnsino salvar(UnidadeEnsino a) {
        return repo.save(a);
    }

    public void deletar(Integer id) {
        repo.deleteById(id);
    }
}
