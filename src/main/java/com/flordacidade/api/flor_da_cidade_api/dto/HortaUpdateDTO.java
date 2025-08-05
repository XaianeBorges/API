package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.Horta.StatusHorta;
import lombok.Data;

@Data
public class HortaUpdateDTO {
    private String nomeHorta;
    private String funcaoUniEnsino;
    private String ocupacaoPrincipal;
    private String endereco;
    private String enderecoAlternativo;
    private Float tamanhoAreaProducao;
    private String caracteristicaGrupo;
    private Integer qntPessoas;
    private String atividadeDescricao;
    private String parceria;
    private StatusHorta statusHorta;
    private Integer idUnidadeEnsino;
    private Integer idAreaClassificacao;
    private Integer idAtividadesProdutivas;
    private Integer idTipoDeHorta;
}
