package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @OneToOne
    @JoinColumn(name = "id_pessoa")
    private Pessoa pessoa;

    private String senha;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

}
