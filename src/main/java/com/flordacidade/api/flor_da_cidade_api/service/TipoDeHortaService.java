package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.TipoDeHorta;
import com.flordacidade.api.flor_da_cidade_api.repository.TipoDeHortaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoDeHortaService {
    @Autowired
    private TipoDeHortaRepository repo;

    public List<TipoDeHorta> listar() {
        return repo.findAll();
    }

    public Optional<TipoDeHorta> buscarPorId(Integer id) {
        return repo.findById(id);
    }

    public TipoDeHorta salvar(TipoDeHorta a) {
        return repo.save(a);
    }

    public void deletar(Integer id) {
        repo.deleteById(id);
    }
}
