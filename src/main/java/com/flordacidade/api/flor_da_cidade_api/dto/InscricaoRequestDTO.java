package com.flordacidade.api.flor_da_cidade_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InscricaoRequestDTO {
    @NotNull(message = "O ID do curso é obrigatório.")
    private Integer idCurso;

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Integer idUsuario;
}
