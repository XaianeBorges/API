package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HortaResponseDTO {
    private Integer idHorta;
    private String nomeHorta;
    private String statusHorta;
    private String funcaoUniEnsino;
    private String ocupacaoPrincipal;
    private String endereco;
    private String enderecoAlternativo;
    private Float tamanhoAreaProducao;
    private String caracteristicaGrupo;
    private Integer qntPessoas;
    private String atividadeDescricao;
    private String parceria;
    private String imagemUrl;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String nomeUnidadeEnsino;
    private String nomeAreaClassificacao;
    private String nomeAtividadesProdutivas;
    private String nomeUsuario;
    private String nomeTipoDeHorta;
}