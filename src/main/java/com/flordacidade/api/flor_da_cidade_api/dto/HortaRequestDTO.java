package com.flordacidade.api.flor_da_cidade_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HortaRequestDTO {

    @NotBlank(message = "O nome da horta é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nomeHorta;

    private String funcaoUniEnsino;
    private String ocupacaoPrincipal;

    @NotBlank(message = "O endereço é obrigatório.")
    private String endereco;

    private String enderecoAlternativo;

    private String caracteristicaGrupo;

    @NotNull(message = "A quantidade de pessoas é obrigatória.")
    @Positive(message = "A quantidade de pessoas deve ser um número positivo.")
    private Integer qntPessoas;

    @NotBlank(message = "A descrição da atividade é obrigatória.")
    private String atividadeDescricao;

    private String parceria;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Integer idUsuario;

    @NotNull(message = "O ID da unidade de ensino é obrigatório.")
    private Integer idUnidadeEnsino;

    @NotNull(message = "O ID da área de classificação é obrigatório.")
    private Integer idAreaClassificacao;

    @NotNull(message = "O ID das atividades produtivas é obrigatório.")
    private Integer idAtividadesProdutivas;

    @NotNull(message = "O ID do tipo de horta é obrigatório.")
    private Integer idTipoDeHorta;
}