package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.RegiaoDTO;
import com.flordacidade.api.flor_da_cidade_api.model.RegiaoModel;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RegiaoMapper {
    RegiaoDTO toDTO(RegiaoModel entity);

    RegiaoModel toEntity(RegiaoDTO dto);

    List<RegiaoDTO> toDTOList(List<RegiaoModel> entities);
}
