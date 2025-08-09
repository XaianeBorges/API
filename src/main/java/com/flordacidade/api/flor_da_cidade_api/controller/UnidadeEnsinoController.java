package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.UnidadeEnsinoDTO;
import com.flordacidade.api.flor_da_cidade_api.service.UnidadeEnsinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades-ensino")
@RequiredArgsConstructor
@Tag(name = "Unidade de Ensino", description = "Operações com unidades de ensino")
public class UnidadeEnsinoController {

    private final UnidadeEnsinoService service;

    @PostMapping
    @Operation(summary = "Salvar nova unidade de ensino")
    @ApiResponse(responseCode = "201", description = "unidade de ensino salva", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UnidadeEnsinoDTO.class)))
    public ResponseEntity<UnidadeEnsinoDTO> salvar(@RequestBody UnidadeEnsinoDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @Operation(summary = "Listar todas as unidades de ensino")
    @ApiResponse(responseCode = "200", description = "Lista de uniddades de ensino retornada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UnidadeEnsinoDTO.class)))
    @GetMapping
    public ResponseEntity<List<UnidadeEnsinoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
    
    @Operation(summary = "Buscar unidade de ensino por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna unidade de ensino", content = @Content(schema = @Schema(implementation = UnidadeEnsinoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Unidade de ensino não encontrada", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeEnsinoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Deletar unidade de ensino por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unindade de ensino excluída com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Unidade de ensino não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
