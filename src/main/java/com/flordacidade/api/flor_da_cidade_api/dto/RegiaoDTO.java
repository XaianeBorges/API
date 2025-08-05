package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RegiaoDTO {
    private Integer idRegiao;
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;
}
