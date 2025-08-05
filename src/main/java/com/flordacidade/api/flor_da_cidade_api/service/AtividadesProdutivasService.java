package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.AtividadesProdutivas;
import com.flordacidade.api.flor_da_cidade_api.repository.AtividadesProdutivasRepository;
import com.flordacidade.api.flor_da_cidade_api.dto.AtividadesProdutivasDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.AtividadesProdutivasMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AtividadesProdutivasService {

    private final AtividadesProdutivasRepository repository;
    private final AtividadesProdutivasMapper mapper;

    public List<AtividadesProdutivasDTO> listarTodos() {
        return mapper.toDTOList(repository.findAll());
    }

    public AtividadesProdutivasDTO salvar(AtividadesProdutivasDTO dto) {
        AtividadesProdutivas entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }

    public AtividadesProdutivasDTO buscarPorId(Integer id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atividades Produtivas não encontradas")));
    }
}
