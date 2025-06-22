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
    @Column(name = "id_horta_validacao") // Adicionei para clareza, mas é opcional
    private Integer idHortaValidacao;

    @Column(name = "data_visita", nullable = false)
    private LocalDateTime dataVisita;

    private String obs;

    @ManyToOne(fetch = FetchType.LAZY) // Usar LAZY é uma boa prática
    @JoinColumn(name = "id_tecnico", nullable = false)
    private TecnicoModel tecnico;

    @ManyToOne(fetch = FetchType.LAZY) // Usar LAZY é uma boa prática
    @JoinColumn(name = "id_horta", nullable = false)
    private Horta horta;
}