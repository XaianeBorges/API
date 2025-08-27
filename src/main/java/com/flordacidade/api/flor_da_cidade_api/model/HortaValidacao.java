package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idHortaValidacao")
@Entity
@Table(name = "horta_validacao")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class HortaValidacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horta_validacao")
    private Integer idHortaValidacao;

    @Column(name = "data_visita", nullable = false)
    private LocalDateTime dataVisita;

    private String obs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico", nullable = false)
    private TecnicoModel tecnico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_horta", nullable = false)
    private Horta horta;
}