package com.flordacidade.api.flor_da_cidade_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "pessoa", uniqueConstraints = {
        @UniqueConstraint(columnNames = "cpf"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "telefone")
})
public class PessoaModel {

    // ======================= INÍCIO DA CORREÇÃO =======================
    // Alinhando este enum com os valores exatos do ENUM do banco de dados.
    public enum Escolaridade {
        SEM_ESCOLARIDADE,
        ENSINO_FUNDAMENTAL_COMPLETO,
        ENSINO_FUNDAMENTAL_INCOMPLETO,
        ENSINO_MEDIO_COMPLETO,
        ENSINO_MEDIO_INCOMPLETO,
        ENSINO_SUPERIOR_COMPLETO,
        ENSINO_SUPERIOR_INCOMPLETO,
        POS_GRADUACAO
    }
    // ======================== FIM DA CORREÇÃO =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPessoa;

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres.")
    @Column(nullable = false, length = 255)
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco.")
    // Valida se o CPF tem exatamente 11 dígitos numéricos
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    @Column(nullable = false, length = 11, unique = true) // Adicionar unique=true aqui ajuda o banco de dados
    private String cpf;

    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento deve ser uma data no passado.")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "O formato do e-mail é inválido.")
    @Size(max = 255)
    @Column(nullable = false, length = 255, unique = true) // Adicionar unique=true
    private String email;

    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres.")
    @Column(length = 255)
    private String endereco;

    @NotBlank(message = "O telefone não pode estar em branco.")
    // Valida se o telefone tem entre 10 e 15 dígitos numéricos (cobrindo formatos
    // com ou sem 9, com ou sem DDI)
    @Pattern(regexp = "\\d{10,15}", message = "O telefone deve conter apenas números, entre 10 e 15 dígitos.")
    @Column(nullable = false, length = 15, unique = true) // Adicionar unique=true
    private String telefone;

    @NotNull(message = "A escolaridade não pode ser nula.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Escolaridade escolaridade;
}