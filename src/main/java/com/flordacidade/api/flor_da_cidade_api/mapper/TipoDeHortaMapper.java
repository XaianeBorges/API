package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.TipoDeHortaDTO;
import com.flordacidade.api.flor_da_cidade_api.model.TipoDeHorta;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoDeHortaMapper {
    TipoDeHortaDTO toDTO(TipoDeHorta entity);

    TipoDeHorta toEntity(TipoDeHortaDTO dto);

    List<TipoDeHortaDTO> toDTOList(List<TipoDeHorta> entities);
}
