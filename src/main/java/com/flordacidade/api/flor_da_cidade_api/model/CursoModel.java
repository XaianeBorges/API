package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "cursos")
@NoArgsConstructor

public class CursoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCurso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAtividade tipoAtividade;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private String local;

    private String fotoBanner;

    @Column(nullable = false)
    private String instituicao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PublicoAlvo publicoAlvo;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Column(nullable = false)
    private LocalDate dataInscInicio;

    @Column(nullable = false)
    private LocalDate dataInscFim;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;

    @Column(nullable = false)
    private Integer maxPessoas;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @Column(nullable = false)
    private Integer cargaHoraria = 0;

    public void setIdCurso(Integer id) {
        this.idCurso = id;
    }

    // Enums
    public enum TipoAtividade {
        Curso, Oficina
    }

    public enum PublicoAlvo {
        Geral, Interno, Comunidade, Estudantes, Idosos
    }

    public enum Turno {
        Manhã, Tarde, Noite
    }

    // Ciclo de vida
    @PrePersist
    protected void onCreate() {
        dataCriacao = dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
