package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.UnidadeEnsinoDTO;
import com.flordacidade.api.flor_da_cidade_api.service.UnidadeEnsinoService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<UnidadeEnsinoDTO> salvar(@RequestBody UnidadeEnsinoDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todas as unidades de ensino")
    public ResponseEntity<List<UnidadeEnsinoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar unidade de ensino por ID")
    public ResponseEntity<UnidadeEnsinoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar unidade de ensino por ID")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
