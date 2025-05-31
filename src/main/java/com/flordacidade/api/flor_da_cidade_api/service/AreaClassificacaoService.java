package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.AreaClassificacao;
import com.flordacidade.api.flor_da_cidade_api.repository.AreaClassificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaClassificacaoService {
    @Autowired
    private AreaClassificacaoRepository repo;

    public List<AreaClassificacao> listar() {
        return repo.findAll();
    }

    public Optional<AreaClassificacao> buscarPorId(Integer id) {
        return repo.findById(id);
    }

    public AreaClassificacao salvar(AreaClassificacao a) {
        return repo.save(a);
    }

    public void deletar(Integer id) {
        repo.deleteById(id);
    }
}