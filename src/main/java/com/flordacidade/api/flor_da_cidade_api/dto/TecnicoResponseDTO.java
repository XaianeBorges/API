package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel.TecnicoStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TecnicoResponseDTO {
    private Integer idTecnico;
    private TecnicoStatus status;
    private String matricula;
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;
    private String nomeRegiao;
    private boolean adm;
}