package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Horta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHorta;

    private String funcaoUniEnsino;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_horta", nullable = false)
    private StatusHorta statusHorta = StatusHorta.PENDENTE;

    private String ocupacaoPrincipal;
    private String endereco;
    private String enderecoAlternativo;
    private float tamanhoAreaProducao;
    private String caracteristicaGrupo;
    private int qntPessoas;
    private String atividadeDescricao;
    private String imagemCaminho;
    private String parceria;

    @ManyToOne
    @JoinColumn(name = "id_unidade_ensino")
    private UnidadeEnsino unidadeEnsino;

    @ManyToOne
    @JoinColumn(name = "id_area_classificacao")
    private AreaClassificacao areaClassificacao;

    @ManyToOne
    @JoinColumn(name = "id_atividades_produtivas")
    private AtividadesProdutivas atividadesProdutivas;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioModel usuario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_de_horta")
    private TipoDeHorta tipoDeHorta;

    private Timestamp dataCriacao;
    private Timestamp dataAtualizacao;

    public enum StatusHorta {
        ATIVA, PENDENTE, VISITA_AGENDADA, INATIVA
    }
}