package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AreaClassificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAreaClassificacao;

    private String nome;
}
