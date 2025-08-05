package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.AtividadesProdutivasDTO;
import com.flordacidade.api.flor_da_cidade_api.service.AtividadesProdutivasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atividades-produtivas")
@RequiredArgsConstructor
@Tag(name = "Atividades Produtivas", description = "Operações com atividades produtivas")
public class AtividadesProdutivasController {

    private final AtividadesProdutivasService service;

    @Operation(summary = "Salvar uma nova atividade produtiva")
    @PostMapping
    public ResponseEntity<AtividadesProdutivasDTO> salvar(@RequestBody AtividadesProdutivasDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @Operation(summary = "Listar todas as atividades produtivas")
    @GetMapping
    public ResponseEntity<List<AtividadesProdutivasDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar atividade produtiva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AtividadesProdutivasDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Deletar atividade produtiva por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
