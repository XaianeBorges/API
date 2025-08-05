package com.flordacidade.api.flor_da_cidade_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AreaClassificacaoDTO {
    private Integer idAreaClassificacao;
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;
}
