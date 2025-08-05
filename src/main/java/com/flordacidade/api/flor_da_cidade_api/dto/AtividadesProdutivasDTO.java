package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class AtividadesProdutivasDTO {
    private Integer idAtividadesProdutivas;
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;
}
