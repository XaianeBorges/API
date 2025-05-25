package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class HortaValidacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHortaValidacao;

    private LocalDateTime dataVisita;
    private String obs;

    @ManyToOne
    @JoinColumn(name = "id_horta")
    private Horta horta;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE;

    public enum Status {
        ATIVA, PENDENTE, VISITA_AGENDADA, INATIVA
    }
}
