package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UnidadeEnsinoDTO {
    private Integer idUnidadeEnsino;
    @NotBlank
    private String nome;
    @NotBlank
    private String endereco;
    @NotBlank
    private String tipo;
}
