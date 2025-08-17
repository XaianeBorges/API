package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel.Escolaridade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioUpdateDTO {
    // Todos os campos são opcionais na atualização
    private String nome;

    @Pattern(regexp = "[0-9]{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpf;

    @Email(message = "Formato de e-mail inválido.")
    private String email;

    private String endereco;

    @Pattern(regexp = "[0-9]{10,15}", message = "O telefone deve conter entre 10 e 15 dígitos.")
    private String telefone;

    @Past(message = "A data de nascimento deve ser no passado.")
    private LocalDate dataNascimento;

    private Escolaridade escolaridade;

}
