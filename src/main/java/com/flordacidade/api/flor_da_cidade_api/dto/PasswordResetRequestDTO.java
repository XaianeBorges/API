package com.flordacidade.api.flor_da_cidade_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PasswordResetRequestDTO {
    @NotBlank
    @Email
    private String email;
}
