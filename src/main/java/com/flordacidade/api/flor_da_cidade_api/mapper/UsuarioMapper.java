package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioCreateDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toResponseDTO(UsuarioModel usuario);

    List<UsuarioResponseDTO> toResponseDTOList(List<UsuarioModel> usuarios);

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "ativo", ignore = true) // Ativo é true por padrão na entidade
    UsuarioModel createDtoToEntity(UsuarioCreateDTO createDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    void updateEntityFromDto(UsuarioUpdateDTO updateDTO, @MappingTarget UsuarioModel usuarioModel);
}
