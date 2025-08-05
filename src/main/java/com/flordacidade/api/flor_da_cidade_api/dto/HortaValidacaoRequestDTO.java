package com.flordacidade.api.flor_da_cidade_api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HortaValidacaoRequestDTO {

    @NotNull(message = "A data da visita é obrigatória.")
    @FutureOrPresent(message = "A data da visita não pode ser no passado.")
    private LocalDateTime dataVisita;

    private String obs;

    @NotNull(message = "O ID do técnico é obrigatório.")
    private Integer idTecnico;

    @NotNull(message = "O ID da horta a ser validada é obrigatório.")
    private Integer idHorta;
}
