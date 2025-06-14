package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

import java.sql.Timestamp;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Data // Cuidado com @Data em entidades JPA, pode causar problemas. @Getter, @Setter,
      // @NoArgsConstructor é mais seguro.
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
    private Float tamanhoAreaProducao;
    private String caracteristicaGrupo;
    private Integer qntPessoas;
    private String atividadeDescricao;
    private String imagemCaminho;
    private String parceria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidade_ensino")
    private UnidadeEnsino unidadeDeEnsino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area_classificacao")
    private AreaClassificacao areaClassificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atividades_produtivas")
    private AtividadesProdutivas atividadesProdutivas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private UsuarioModel usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_de_horta")
    private TipoDeHorta tipoDeHorta;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    public enum StatusHorta {
        ATIVA, PENDENTE, VISITA_AGENDADA, INATIVA
    }
}