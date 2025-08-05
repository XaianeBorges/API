package com.flordacidade.api.flor_da_cidade_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewPasswordRequestDTO {
    @NotBlank
    private String token;
    @NotBlank
    @Size(min = 8)
    private String novaSenha;
}
