package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.RegiaoDTO;
import com.flordacidade.api.flor_da_cidade_api.service.RegiaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regioes")
@RequiredArgsConstructor
@Tag(name = "Região", description = "Operações relacionadas às regiões")
public class RegiaoController {

    private final RegiaoService service;

    @PostMapping
    @Operation(summary = "Salvar nova região")
    public ResponseEntity<RegiaoDTO> salvar(@RequestBody RegiaoDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todas as regiões")
    public ResponseEntity<List<RegiaoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar região por ID")
    public ResponseEntity<RegiaoDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar região por ID")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}