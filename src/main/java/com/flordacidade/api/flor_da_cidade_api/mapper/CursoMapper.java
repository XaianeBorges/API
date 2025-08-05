package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.CursoRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.CursoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.CursoUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CursoMapper {

    @Mapping(source = "fotoBanner", target = "fotoBannerUrl", qualifiedByName = "bannerToUrl")
    CursoResponseDTO toResponseDTO(CursoModel curso);

    List<CursoResponseDTO> toResponseDTOList(List<CursoModel> cursos);

    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "fotoBanner", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    CursoModel requestDtoToEntity(CursoRequestDTO requestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idCurso", ignore = true)
    @Mapping(target = "fotoBanner", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    void updateEntityFromDto(CursoUpdateDTO updateDTO, @MappingTarget CursoModel cursoModel);

    @Named("bannerToUrl")
    default String bannerToUrl(String bannerFileName) {
        if (bannerFileName == null || bannerFileName.isBlank()) {
            return null;
        }
        return "/api/arquivos/banners/" + bannerFileName;
    }
}
