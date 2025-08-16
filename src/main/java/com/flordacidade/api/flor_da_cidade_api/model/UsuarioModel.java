// Caminho do Arquivo: src/main/java/com/flordacidade/api/flor_da_cidade_api/model/UsuarioModel.java

package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres.")
    @Column(nullable = false, length = 255)
    private String nome;

    @NotBlank(message = "O CPF não pode estar em branco.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "O formato do e-mail é inválido.")
    @Size(max = 255)
    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres.")
    @Column(length = 255)
    private String endereco;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime atualizadoEm;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @NotBlank(message = "O telefone não pode estar em branco.")
    @Pattern(regexp = "\\d{10,15}", message = "O telefone deve conter apenas números, entre 10 e 15 dígitos.")
    @Column(nullable = false, length = 15, unique = true)
    private String telefone;

    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento deve ser uma data no passado.")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotNull(message = "A escolaridade não pode ser nula.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Escolaridade escolaridade;

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
}
