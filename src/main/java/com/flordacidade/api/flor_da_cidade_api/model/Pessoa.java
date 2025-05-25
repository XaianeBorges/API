package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPessoa;

    private String nome;
    private String cpf;

    private LocalDate dataNascimento;
    private String email;
    private String endereco;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Escolaridade escolaridade;

    public enum Escolaridade {
        SEM_ESCOLARIDADE,
        ENSINO_FUNDAMENTAL_COMPLETO,
        ENSINO_FUNDAMENTAL_INCOMPLETO,
        ENSINO_MEDIO_COMPLETO,
        ENSINO_MEDIO_INCOMPLETO,
        ENSINO_SUPERIOR_INCOMPLETO,
        ENSINO_SUPERIOR_COMPLETO,
        POS_GRADUACAO
    }
}