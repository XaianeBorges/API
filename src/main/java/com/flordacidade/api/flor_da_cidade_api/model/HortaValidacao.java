package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "horta_validacao")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@NoArgsConstructor
@AllArgsConstructor
public class HortaValidacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHortaValidacao;

    @Column(name = "data_visita", nullable = false)
    private LocalDateTime dataVisita;

    private String obs;

    @ManyToOne
    @JoinColumn(name = "id_tecnico", nullable = false)
    private TecnicoModel tecnico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_horta")
    private Horta horta;
}