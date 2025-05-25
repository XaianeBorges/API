package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.UsuarioResponseDTO;
import com.flordacidade.api.flor_da_cidade_api.model.Usuario;
import com.flordacidade.api.flor_da_cidade_api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listAll() {
        return service.getAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(this::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(
            @RequestBody @Valid UsuarioDTO dto) {

        Usuario u = service.createFromDTO(dto);
        return ResponseEntity
                .ok(toResponseDTO(u));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody @Valid UsuarioDTO dto) {

        Usuario u = service.updateFromDTO(id, dto);
        return ResponseEntity
                .ok(toResponseDTO(u));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    private UsuarioResponseDTO toResponseDTO(Usuario u) {
        return service.toResponseDTO(u);
    }
}
