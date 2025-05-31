package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.AtividadesProdutivas;
import com.flordacidade.api.flor_da_cidade_api.repository.AtividadesProdutivasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtividadesProdutivasService {
    @Autowired
    private AtividadesProdutivasRepository repo;

    public List<AtividadesProdutivas> listar() {
        return repo.findAll();
    }

    public Optional<AtividadesProdutivas> buscarPorId(Integer id) {
        return repo.findById(id);
    }

    public AtividadesProdutivas salvar(AtividadesProdutivas a) {
        return repo.save(a);
    }

    public void deletar(Integer id) {
        repo.deleteById(id);
    }
}
