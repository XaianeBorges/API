package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;

import java.time.LocalDate;
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

    // nomes (existentes)
    private String nomeUnidadeEnsino;
    private String nomeAreaClassificacao;
    private String nomeAtividadesProdutivas;
    private String nomeUsuario;
    private String nomeTipoDeHorta;

    // IDs das entidades relacionadas (necessários para a tela de edição)
    private Integer idUnidadeEnsino;
    private Integer idAreaClassificacao;
    private Integer idAtividadesProdutivas;
    private Integer idUsuario;
    private Integer idTipoDeHorta;

    // Detalhes adicionais do usuário exigidos pelo front
    private String usuarioCpf;
    private LocalDate usuarioDataNascimento;
    private String usuarioTelefone;
    private String usuarioEmail;
}