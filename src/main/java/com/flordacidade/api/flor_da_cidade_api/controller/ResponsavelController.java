package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.Responsavel;
import com.flordacidade.api.flor_da_cidade_api.service.ResponsavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/responsavel")
public class ResponsavelController {

    @Autowired
    private ResponsavelService responsavelService;

    @GetMapping
    public List<Responsavel> listarResponsaveis() {
        return responsavelService.listarResponsaveis();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Responsavel> buscarPorId(@PathVariable Integer id) {
        return responsavelService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Responsavel> salvarResponsavel(@RequestBody Responsavel responsavel) {
        return ResponseEntity.ok(responsavelService.salvarResponsavel(responsavel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Responsavel> atualizarResponsavel(@PathVariable Integer id, @RequestBody Responsavel responsavelAtualizado) {
        return ResponseEntity.ok(responsavelService.atualizarResponsavel(id, responsavelAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarResponsavel(@PathVariable Integer id) {
        responsavelService.deletarResponsavel(id);
        return ResponseEntity.noContent().build();
    }
}
