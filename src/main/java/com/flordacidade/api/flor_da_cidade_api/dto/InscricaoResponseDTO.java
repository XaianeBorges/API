package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InscricaoResponseDTO {
    private Integer idInscricao;
    private LocalDateTime dataInscricao;

    private Integer idUsuario;
    private String nomeUsuario;
    private String emailUsuario;

    private Integer idCurso;
    private String nomeCurso;
}
