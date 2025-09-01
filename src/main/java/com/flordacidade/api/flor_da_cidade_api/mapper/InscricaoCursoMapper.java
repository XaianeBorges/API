package com.flordacidade.api.flor_da_cidade_api.mapper;

import com.flordacidade.api.flor_da_cidade_api.dto.InscricaoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InscricaoCursoMapper {

    @Mapping(source = "id", target = "idInscricao")
    @Mapping(source = "curso.idCurso", target = "idCurso")
    @Mapping(source = "curso.nome", target = "nomeCurso")
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "usuario.nome", target = "nomeUsuario")
    @Mapping(source = "usuario.email", target = "emailUsuario")
    @Mapping(source = "usuario.telefone", target = "telefoneUsuario")
    @Mapping(source = "usuario.cpf", target = "cpfUsuario")
    @Mapping(source = "usuario.dataNascimento", target = "dataNascimentoUsuario")
    @Mapping(source = "usuario.escolaridade", target = "escolaridadeUsuario")
    InscricaoResponseDTO toResponseDTO(InscricaoCursoModel inscricao);

    List<InscricaoResponseDTO> toResponseDTOList(List<InscricaoCursoModel> inscricoes);
}
