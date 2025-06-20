package com.flordacidade.api.flor_da_cidade_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "tecnico")
public class TecnicoModel {

    // --- INÍCIO DA ALTERAÇÃO ---
    // O Enum foi movido para dentro da classe TecnicoModel.
    // Isso resolve o erro "TecnicoStatus cannot be resolved" sem criar um novo
    // arquivo.
    public enum TecnicoStatus {
        ATIVO,
        INATIVO
    }
    // --- FIM DA ALTERAÇÃO ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnico")
    private Integer idTecnico;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TecnicoStatus status = TecnicoStatus.ATIVO; // Agora isso funciona corretamente.

    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "regiao")
    private String regiao;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "nome", nullable = false, length = 45)
    private String nome;
}
