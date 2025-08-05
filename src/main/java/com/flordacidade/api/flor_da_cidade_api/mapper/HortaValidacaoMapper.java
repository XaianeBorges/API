package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.HortaValidacaoRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaValidacaoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HortaValidacaoMapper {

    @Mapping(source = "tecnico.nome", target = "nomeTecnico")
    @Mapping(source = "horta.idHorta", target = "idHorta")
    @Mapping(source = "horta.nomeHorta", target = "nomeHorta")
    HortaValidacaoResponseDTO toResponseDTO(HortaValidacao hortaValidacao);

    @Mapping(target = "idHortaValidacao", ignore = true)
    @Mapping(target = "tecnico", ignore = true)
    @Mapping(target = "horta", ignore = true)
    HortaValidacao toEntity(HortaValidacaoRequestDTO requestDTO);
}
