// src/main/java/com/flordacidade/api/flor_da_cidade_api/model/PessoaModel.java
package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "pessoa", uniqueConstraints = {
                @UniqueConstraint(columnNames = "cpf"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "telefone")
})
@Data
public class PessoaModel {

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
        @Column(nullable = false, length = 50)
        private Escolaridade escolaridade;
}
