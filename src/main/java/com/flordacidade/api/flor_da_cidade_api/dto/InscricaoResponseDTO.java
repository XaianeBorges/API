package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel.Escolaridade;

@Data
public class InscricaoResponseDTO {
    private Integer idInscricao;
    private LocalDateTime dataInscricao;

    private Integer idUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String telefoneUsuario;
    private String cpfUsuario;
    private LocalDate dataNascimentoUsuario;
    private Escolaridade escolaridadeUsuario;

    private Integer idCurso;
    private String nomeCurso;
}
