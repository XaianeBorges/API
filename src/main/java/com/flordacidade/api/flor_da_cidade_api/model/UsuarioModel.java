// Caminho do Arquivo: src/main/java/com/flordacidade/api/flor_da_cidade_api/model/UsuarioModel.java

package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    // ALTERAÇÃO APLICADA: Mudança de FetchType.LAZY para FetchType.EAGER
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @JoinColumn(name = "id_pessoa", nullable = false)
    @NotNull
    private PessoaModel pessoa;

    @Column(name = "senha", nullable = false)
    @NotNull
    @Size(min = 8)
    private String senha;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime atualizadoEm;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "matricula")
    private String matricula;
}