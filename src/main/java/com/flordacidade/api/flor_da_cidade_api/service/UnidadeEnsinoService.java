package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.UnidadeEnsinoDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.UnidadeEnsinoMapper;
import com.flordacidade.api.flor_da_cidade_api.model.UnidadeEnsino;
import com.flordacidade.api.flor_da_cidade_api.repository.UnidadeEnsinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadeEnsinoService {

    private final UnidadeEnsinoRepository repository;
    private final UnidadeEnsinoMapper mapper;

    public List<UnidadeEnsinoDTO> listarTodos() {
        return mapper.toDTOList(repository.findAll());
    }

    public UnidadeEnsinoDTO salvar(UnidadeEnsinoDTO dto) {
        UnidadeEnsino entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }

    public UnidadeEnsinoDTO buscarPorId(Integer id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado")));
    }
}
