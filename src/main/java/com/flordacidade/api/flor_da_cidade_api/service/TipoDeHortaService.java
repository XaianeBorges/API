package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.dto.TipoDeHortaDTO;
import com.flordacidade.api.flor_da_cidade_api.mapper.TipoDeHortaMapper;
import com.flordacidade.api.flor_da_cidade_api.model.TipoDeHorta;
import com.flordacidade.api.flor_da_cidade_api.repository.TipoDeHortaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoDeHortaService {

    private final TipoDeHortaRepository repository;
    private final TipoDeHortaMapper mapper;

    public List<TipoDeHortaDTO> listarTodos() {
        return mapper.toDTOList(repository.findAll());
    }

    public TipoDeHortaDTO salvar(TipoDeHortaDTO dto) {
        TipoDeHorta entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }

    public TipoDeHortaDTO buscarPorId(Integer id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de Horta não encontrado")));
    }
}
