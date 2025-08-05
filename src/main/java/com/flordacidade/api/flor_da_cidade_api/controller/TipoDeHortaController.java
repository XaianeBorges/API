package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.TipoDeHortaDTO;
import com.flordacidade.api.flor_da_cidade_api.service.TipoDeHortaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-horta")
@RequiredArgsConstructor
@Tag(name = "Tipo de Horta", description = "Operações com tipos de horta")
public class TipoDeHortaController {

    private final TipoDeHortaService service;

    @PostMapping
    @Operation(summary = "Salvar novo tipo de horta")
    public ResponseEntity<TipoDeHortaDTO> salvar(@RequestBody TipoDeHortaDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos os tipos de horta")
    public ResponseEntity<List<TipoDeHortaDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de horta por ID")
    public ResponseEntity<TipoDeHortaDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tipo de horta por ID")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
