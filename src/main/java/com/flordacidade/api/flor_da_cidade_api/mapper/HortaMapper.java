package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.HortaRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.model.Horta; // Certifique-se que o import do seu Model/Entidade está correto
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HortaMapper {

    @Mapping(source = "usuario.nome", target = "nomeUsuario")
    @Mapping(source = "unidadeDeEnsino.nome", target = "nomeUnidadeEnsino")
    @Mapping(source = "areaClassificacao.nome", target = "nomeAreaClassificacao")
    @Mapping(source = "atividadesProdutivas.nome", target = "nomeAtividadesProdutivas")
    @Mapping(source = "tipoDeHorta.nome", target = "nomeTipoDeHorta")
    @Mapping(source = "imagemCaminho", target = "imagemUrl", qualifiedByName = "caminhoParaUrl")
    HortaResponseDTO toResponseDTO(Horta horta);

    List<HortaResponseDTO> toResponseDTOList(List<Horta> hortas);

    @Mapping(target = "idHorta", ignore = true)
    @Mapping(target = "statusHorta", ignore = true)
    @Mapping(target = "imagemCaminho", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "unidadeDeEnsino", ignore = true)
    @Mapping(target = "areaClassificacao", ignore = true)
    @Mapping(target = "atividadesProdutivas", ignore = true)
    @Mapping(target = "tipoDeHorta", ignore = true)
    Horta requestDtoToEntity(HortaRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idHorta", ignore = true)
    @Mapping(target = "imagemCaminho", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "usuario", ignore = true) // O dono original da horta não deve ser mudado aqui
    @Mapping(target = "unidadeDeEnsino", ignore = true) // Relações são tratadas no serviço
    @Mapping(target = "areaClassificacao", ignore = true)
    @Mapping(target = "atividadesProdutivas", ignore = true)
    @Mapping(target = "tipoDeHorta", ignore = true)
    void updateEntityFromDto(HortaUpdateDTO dto, @MappingTarget Horta horta);

    @Named("caminhoParaUrl")
    default String caminhoParaUrl(String caminho) {
        if (caminho == null || caminho.isBlank() || "folhin.png".equals(caminho)) {
            // Você pode querer retornar uma URL para uma imagem padrão
            return "/api/arquivos/hortas/folhin.png";
        }
        // Idealmente, a URL base deveria vir de um arquivo de configuração
        // (application.properties)
        return "/api/arquivos/hortas/" + caminho;
    }
}
