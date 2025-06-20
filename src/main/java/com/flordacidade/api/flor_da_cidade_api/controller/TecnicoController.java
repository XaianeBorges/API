package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.service.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final TecnicoService service;

    @Autowired
    public TecnicoController(TecnicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TecnicoModel>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoModel> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TecnicoModel> create(@RequestBody TecnicoModel tecnico) {
        return ResponseEntity.ok(service.create(tecnico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TecnicoModel> update(@PathVariable Integer id, @RequestBody TecnicoModel tecnico) {
        return service.update(id, tecnico)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}