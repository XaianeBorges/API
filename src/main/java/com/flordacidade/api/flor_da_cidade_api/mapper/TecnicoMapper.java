package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.LoginResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoCreateDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TecnicoMapper {

    @Mapping(source = "regiao.nome", target = "nomeRegiao")
    TecnicoResponseDTO toResponseDTO(TecnicoModel tecnico);

    List<TecnicoResponseDTO> toResponseDTOList(List<TecnicoModel> tecnicos);
    
    LoginResponseDTO toLoginResponseDTO(TecnicoModel tecnico);

    @Mapping(target = "idTecnico", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "regiao", ignore = true)
    @Mapping(target = "resetPasswordToken", ignore = true)
    @Mapping(target = "resetPasswordTokenExpiry", ignore = true)
    TecnicoModel createDtoToEntity(TecnicoCreateDTO createDTO);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idTecnico", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "regiao", ignore = true)
    @Mapping(target = "resetPasswordToken", ignore = true)
    @Mapping(target = "resetPasswordTokenExpiry", ignore = true)
    void updateEntityFromDto(TecnicoUpdateDTO updateDTO, @MappingTarget TecnicoModel tecnico);
}