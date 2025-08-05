package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.AreaClassificacaoDTO;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.model.AreaClassificacao;
import com.flordacidade.api.flor_da_cidade_api.repository.AreaClassificacaoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AreaClassificacaoService {

    private final AreaClassificacaoRepository repository;

    @Transactional(readOnly = true)
    public List<AreaClassificacao> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AreaClassificacao> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public AreaClassificacao salvar(AreaClassificacaoDTO dto) {
        AreaClassificacao entidade = new AreaClassificacao();
        entidade.setNome(dto.getNome());
        return repository.save(entidade);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Área de Classificação não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}