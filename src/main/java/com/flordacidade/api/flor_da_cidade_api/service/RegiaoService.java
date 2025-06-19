package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.RegiaoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.RegiaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RegiaoService {

    private final RegiaoRepository regiaoRepository;

    @Autowired
    public RegiaoService(RegiaoRepository regiaoRepository) {
        this.regiaoRepository = regiaoRepository;
    }

    public List<RegiaoModel> listarTodas() {
        return regiaoRepository.findAll();
    }

    public Optional<RegiaoModel> buscarPorId(Integer id) {
        return regiaoRepository.findById(id);
    }

    public RegiaoModel salvar(RegiaoModel regiao) {
        return regiaoRepository.save(regiao);
    }

    public void deletar(Integer id) {
        regiaoRepository.deleteById(id);
    }
}