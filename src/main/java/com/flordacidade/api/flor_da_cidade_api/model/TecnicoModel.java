// src/main/java/com/flordacidade/api/flordacidade/api/flor_da_cidade_api/model/TecnicoModel.java
package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tecnico")
public class TecnicoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnico")
    private Integer idTecnico;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TecnicoStatus status = TecnicoStatus.ATIVO;

    @Column(name = "matricula", nullable = false)
    private String matricula;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "regiao")
    private String regiao;

    @Column(name = "nome", nullable = false, length = 45)
    private String nome;
}
