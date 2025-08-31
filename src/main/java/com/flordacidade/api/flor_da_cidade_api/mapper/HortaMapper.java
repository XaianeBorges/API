package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.HortaComUsuarioDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.model.Horta;
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
    @Mapping(source = "usuario.cpf", target = "cpfUsuario")
    @Mapping(source = "usuario.email", target = "emailUsuario")
    @Mapping(source = "usuario.telefone", target = "telefoneUsuario")
    @Mapping(source = "usuario.dataNascimento", target = "dataNascimentoUsuario")
    @Mapping(source = "usuario.endereco", target = "enderecoUsuario")
    @Mapping(source = "usuario.escolaridade", target = "escolaridadeUsuario")
    @Mapping(source = "unidadeEnsino.nome", target = "nomeUnidadeEnsino")
    @Mapping(source = "areaClassificacao.nome", target = "nomeAreaClassificacao")
    @Mapping(source = "atividadesProdutivas.nome", target = "nomeAtividadesProdutivas")
    @Mapping(source = "tipoDeHorta.nome", target = "nomeTipoDeHorta")
    @Mapping(source = "imagemCaminho", target = "imagemUrl", qualifiedByName = "caminhoParaUrl")

    @Mapping(source = "unidadeEnsino.idUnidadeEnsino", target = "idUnidadeEnsino")
    @Mapping(source = "areaClassificacao.idAreaClassificacao", target = "idAreaClassificacao")
    @Mapping(source = "atividadesProdutivas.idAtividadesProdutivas", target = "idAtividadesProdutivas")
    @Mapping(source = "tipoDeHorta.idTipoDeHorta", target = "idTipoDeHorta")
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    HortaResponseDTO toResponseDTO(Horta horta);

    List<HortaResponseDTO> toResponseDTOList(List<Horta> hortas);

    @Mapping(target = "idHorta", ignore = true)
    @Mapping(target = "statusHorta", ignore = true)
    @Mapping(target = "imagemCaminho", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "unidadeEnsino", ignore = true)
    @Mapping(target = "areaClassificacao", ignore = true)
    @Mapping(target = "atividadesProdutivas", ignore = true)
    @Mapping(target = "tipoDeHorta", ignore = true)
    Horta requestDtoToEntity(HortaRequestDTO dto);

    @Mapping(source = "usuario.nome", target = "nomeUsuario")
    @Mapping(source = "usuario.email", target = "emailUsuario")
    @Mapping(source = "usuario.telefone", target = "telefoneUsuario")
    HortaComUsuarioDTO toHortaComUsuarioDTO(Horta horta);

    // NOVO MÉTODO PARA CONVERTER UMA LISTA DE HORTAS
    List<HortaComUsuarioDTO> toHortaComUsuarioDTOList(List<Horta> hortas);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idHorta", ignore = true)
    @Mapping(target = "imagemCaminho", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "unidadeEnsino", ignore = true)
    @Mapping(target = "areaClassificacao", ignore = true)
    @Mapping(target = "atividadesProdutivas", ignore = true)
    @Mapping(target = "tipoDeHorta", ignore = true)
    void updateEntityFromDto(HortaUpdateDTO dto, @MappingTarget Horta horta);

    @Named("caminhoParaUrl")
    default String caminhoParaUrl(String caminho) {
        if (caminho == null || caminho.isBlank() || "folhin.png".equals(caminho)) {
            return "/uploads/imagem/folhin.png";
        }
        return "/uploads/imagem/" + caminho;
    }
}
