package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.AreaClassificacaoDTO;
import com.flordacidade.api.flor_da_cidade_api.model.AreaClassificacao;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AreaClassificacaoMapper {
    AreaClassificacaoDTO toDTO(AreaClassificacao entity);

    AreaClassificacao toEntity(AreaClassificacaoDTO dto);

    List<AreaClassificacaoDTO> toDTOList(List<AreaClassificacao> entities);
}
