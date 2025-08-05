package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HortaValidacaoResponseDTO {

    private Integer idHortaValidacao;
    private LocalDateTime dataVisita;
    private String obs;
    private String nomeTecnico;
    private Integer idHorta;
    private String nomeHorta;
}
