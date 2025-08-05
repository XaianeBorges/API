package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.HortaValidacaoRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.HortaValidacaoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.service.HortaValidacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hortas/validacoes")
@RequiredArgsConstructor
@Tag(name = "Validações de Hortas", description = "Endpoints para registrar a validação técnica de uma horta")
public class HortaValidacaoController {

    private final HortaValidacaoService service;

    @Operation(summary = "Registra uma nova validação técnica para uma horta", description = "Cria um registro de visita técnica e, como efeito, atualiza o status da horta associada para 'ATIVA'.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Validação registrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HortaValidacaoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos (ex: data no passado, IDs nulos)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Horta ou Técnico não encontrado com o ID fornecido", content = @Content)
    })
    @PostMapping
    public ResponseEntity<HortaValidacaoResponseDTO> validar(
            @Valid @RequestBody HortaValidacaoRequestDTO validacaoRequestDTO) {
        HortaValidacaoResponseDTO responseDTO = service.validarHorta(validacaoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
