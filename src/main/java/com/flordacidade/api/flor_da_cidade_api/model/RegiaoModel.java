package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "regiao") 
public class RegiaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regiao") 
    private Integer idRegiao;

    @Column(name = "nome", nullable = false, unique = true, length = 500)
    private String nome;

}