package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "horta_validacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HortaValidacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHortaValidacao;

    @Column(name = "data_visita", nullable = false)
    private LocalDateTime dataVisita;

    private String obs;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_horta")
    private Horta horta;
}