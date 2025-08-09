package com.flordacidade.api.flor_da_cidade_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TecnicoCreateDTO {
    @NotBlank(message = "A matrícula é obrigatória.")
    private String matricula;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    private String senha;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    private String email;

    @NotNull(message = "O ID da região é obrigatório.")
    private Integer idRegiao;

    private boolean adm = false;
}