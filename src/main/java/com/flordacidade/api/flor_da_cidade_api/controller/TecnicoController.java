package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.service.TecnicoService;
import jakarta.validation.Valid; // Se você tiver anotações de validação na entidade
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.EntityNotFoundException; // Import para usar no update e delete

import java.util.List;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    @Autowired
    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @GetMapping
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')") // Exemplo
    public ResponseEntity<List<TecnicoModel>> getAll() {
        // ALERTA: Retorna a entidade completa, incluindo o campo senha.
        return ResponseEntity.ok(tecnicoService.getAll());
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN') or @customSecurityService.isSelfTecnico(authentication, #id)") // Exemplo
    public ResponseEntity<TecnicoModel> getById(@PathVariable Integer id) {
        // ALERTA: Retorna a entidade completa, incluindo o campo senha.
        return tecnicoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<TecnicoModel> create(@Valid @RequestBody TecnicoModel tecnico) {
        try {
            TecnicoModel createdTecnico = tecnicoService.create(tecnico);
            // ALERTA: Retorna a entidade completa, incluindo o campo senha (mesmo que hasheada).
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTecnico);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN') or @customSecurityService.isSelfTecnico(authentication, #id)")
    public ResponseEntity<TecnicoModel> update(@PathVariable Integer id, @Valid @RequestBody TecnicoModel tecnicoDetails) {
        try {
            return tecnicoService.update(id, tecnicoDetails)
                    .map(updatedTecnico -> {
                        // ALERTA: Retorna a entidade completa, incluindo o campo senha.
                        return ResponseEntity.ok(updatedTecnico);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) { // Captura EntityNotFoundException do service
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            tecnicoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) { // Captura EntityNotFoundException do service
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}