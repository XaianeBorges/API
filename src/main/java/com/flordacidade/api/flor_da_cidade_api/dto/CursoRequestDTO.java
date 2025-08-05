package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CursoRequestDTO {

    @NotNull(message = "O tipo de atividade é obrigatório.")
    private TipoAtividade tipoAtividade;

    @NotBlank(message = "O nome do curso é obrigatório.")
    @Size(max = 255)
    private String nome;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotBlank(message = "O local é obrigatório.")
    private String local;

    @NotBlank(message = "A instituição é obrigatória.")
    private String instituicao;

    @NotNull(message = "O público-alvo é obrigatório.")
    private PublicoAlvo publicoAlvo;

    @NotNull(message = "A data de início do curso é obrigatória.")
    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim do curso é obrigatória.")
    @Future(message = "A data de fim deve ser no futuro.")
    private LocalDate dataFim;

    @NotNull(message = "A data de início das inscrições é obrigatória.")
    private LocalDate dataInscInicio;

    @NotNull(message = "A data de fim das inscrições é obrigatória.")
    private LocalDate dataInscFim;

    @NotNull(message = "O turno é obrigatório.")
    private Turno turno;

    @NotNull(message = "O número máximo de pessoas é obrigatório.")
    @Positive(message = "O número máximo de pessoas deve ser positivo.")
    private Integer maxPessoas;

    @NotNull(message = "A carga horária é obrigatória.")
    @PositiveOrZero(message = "A carga horária não pode ser negativa.")
    private Integer cargaHoraria;
}
