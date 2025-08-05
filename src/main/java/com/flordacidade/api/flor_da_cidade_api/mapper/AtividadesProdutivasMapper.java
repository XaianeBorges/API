package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.AtividadesProdutivasDTO;
import com.flordacidade.api.flor_da_cidade_api.model.AtividadesProdutivas;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AtividadesProdutivasMapper {
    AtividadesProdutivasDTO toDTO(AtividadesProdutivas entity);

    AtividadesProdutivas toEntity(AtividadesProdutivasDTO dto);

    List<AtividadesProdutivasDTO> toDTOList(List<AtividadesProdutivas> entities);
}
