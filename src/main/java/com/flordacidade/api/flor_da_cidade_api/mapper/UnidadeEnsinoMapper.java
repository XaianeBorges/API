package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.UnidadeEnsinoDTO;
import com.flordacidade.api.flor_da_cidade_api.model.UnidadeEnsino;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UnidadeEnsinoMapper {
    UnidadeEnsinoDTO toDTO(UnidadeEnsino entity);

    UnidadeEnsino toEntity(UnidadeEnsinoDTO dto);

    List<UnidadeEnsinoDTO> toDTOList(List<UnidadeEnsino> entities);
}
