package com.flordacidade.api.flor_da_cidade_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class AreaClassificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAreaClassificacao;

    private String nome;
}
