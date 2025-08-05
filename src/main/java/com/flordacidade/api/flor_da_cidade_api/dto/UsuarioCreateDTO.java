package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel.Escolaridade;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioCreateDTO {
    @NotBlank(message = "O nome não pode estar em branco.")
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco.")
    @Pattern(regexp = "[0-9]{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpf;

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "Formato de e-mail inválido.")
    private String email;

    private String endereco;

    @NotBlank(message = "O telefone não pode estar em branco.")
    @Pattern(regexp = "[0-9]{10,15}", message = "O telefone deve conter entre 10 e 15 dígitos.")
    private String telefone;

    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento deve ser no passado.")
    private LocalDate dataNascimento;

    @NotNull(message = "A escolaridade não pode ser nula.")
    private Escolaridade escolaridade;
}
