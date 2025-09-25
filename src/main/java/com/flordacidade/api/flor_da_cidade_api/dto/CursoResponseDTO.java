package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CursoResponseDTO {
    private Integer idCurso;
    private String tipoAtividade;
    private String nome;
    private String descricao;
    private String local;
    private String fotoBannerUrl; 
    private String instituicao;
    private String publicoAlvo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate dataInscInicio;
    private LocalDate dataInscFim;
    private String status;
    private String turno;
    private Integer maxPessoas;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Integer cargaHoraria;
}
