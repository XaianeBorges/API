package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CursoUpdateDTO {

    private TipoAtividade tipoAtividade;
    private String nome;
    private String descricao;
    private String local;
    private String instituicao;
    private PublicoAlvo publicoAlvo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate dataInscInicio;
    private LocalDate dataInscFim;
    private String status;
    private Turno turno;
    private Integer maxPessoas;
    private Integer cargaHoraria;
}
