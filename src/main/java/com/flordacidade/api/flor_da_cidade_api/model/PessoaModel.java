package com.flordacidade.api.flor_da_cidade_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(length = 255)
    private String endereco;

    @Column(nullable = false, length = 15)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // O length não é mais estritamente necessário aqui, mas não prejudica
    private Escolaridade escolaridade;
}