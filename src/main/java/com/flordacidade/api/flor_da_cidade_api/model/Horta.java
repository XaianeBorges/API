package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "horta")
@Data
public class Horta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horta")
    private Integer idHorta;

    @Column(name = "nome_horta")
    private String nomeHorta;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_horta", nullable = false)
    private StatusHorta statusHorta;

    @Column(name = "funcao_uni_ensino")
    private String funcaoUniEnsino;

    @Column(name = "ocupacao_principal")
    private String ocupacaoPrincipal;

    @Column(name = "endereco", nullable = false)
    private String endereco;

    @Column(name = "endereco_alternativo")
    private String enderecoAlternativo;

    @Column(name = "tamanho_area_producao", nullable = false)
    private Float tamanhoAreaProducao;

    @Column(name = "caracteristica_grupo")
    private String caracteristicaGrupo;

    @Column(name = "qnt_pessoas", nullable = false)
    private Integer qntPessoas;

    @Column(name = "atividade_descricao", nullable = false)
    private String atividadeDescricao;

    @Column(name = "imagem_caminho", nullable = false)
    private String imagemCaminho;

    @Column(name = "parceria")
    private String parceria;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidade_ensino", nullable = false)
    private UnidadeEnsino unidadeEnsino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area_classificacao", nullable = false)
    private AreaClassificacao areaClassificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atividades_produtivas", nullable = false)
    private AtividadesProdutivas atividadesProdutivas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioModel usuario; // Usando UsuarioModel para consistência

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_de_horta", nullable = false)
    private TipoDeHorta tipoDeHorta;

    // --- ENUM para Status ---
    public enum StatusHorta {
        ATIVA, PENDENTE, INATIVA, VISITA_AGENDADA
    }
}