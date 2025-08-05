package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private Integer idTecnico;
    private String nome;
    private String matricula;
    private boolean isAdm;
}
