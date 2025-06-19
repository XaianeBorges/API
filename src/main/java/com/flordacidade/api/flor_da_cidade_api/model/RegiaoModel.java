package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "regiao") // Mapeia esta classe para a tabela 'regiao'
public class RegiaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regiao") // Mapeia este campo para a coluna 'id_regiao'
    private Integer idRegiao;

    // Assumindo que sua tabela 'regiao' também tem uma coluna para o nome.
    // Se o nome da coluna for diferente, ajuste o `name = "..."`.
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    // Adicione outros campos da tabela 'regiao' aqui, se houver.
}