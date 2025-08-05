package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.RegiaoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.RegiaoRepository;
import com.flordacidade.api.flor_da_cidade_api.dto.RegiaoDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.RegiaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegiaoService {

    private final RegiaoRepository repository;
    private final RegiaoMapper mapper;

    public List<RegiaoDTO> listarTodos() {
        return mapper.toDTOList(repository.findAll());
    }

    public RegiaoDTO salvar(RegiaoDTO dto) {
        RegiaoModel entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }

    public RegiaoDTO buscarPorId(Integer id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Região não encontrada")));
    }
}