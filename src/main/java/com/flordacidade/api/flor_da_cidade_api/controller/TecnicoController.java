// src/main/java/com/flordacidade/api/flor_da_cidade_api/controller/TecnicoController.java
package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.TecnicoResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.model.Tecnico;
import com.flordacidade.api.flor_da_cidade_api.service.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final TecnicoService service;

    public TecnicoController(TecnicoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TecnicoResponseDTO> listAll() {
        return service.getAll().stream()
                .map(service::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(service::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TecnicoResponseDTO> create(
            @RequestBody @Valid TecnicoDTO dto) {
        Tecnico t = service.createFromDTO(dto);
        return ResponseEntity.ok(service.toResponseDTO(t));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid TecnicoDTO dto) {
        Tecnico t = service.updateFromDTO(id, dto);
        return ResponseEntity.ok(service.toResponseDTO(t));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}