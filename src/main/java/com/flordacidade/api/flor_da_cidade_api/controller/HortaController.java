package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import com.flordacidade.api.flor_da_cidade_api.model.Horta.StatusHorta;
import com.flordacidade.api.flor_da_cidade_api.service.HortaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hortas")
public class HortaController {

    @Autowired
    private HortaService hortaService;

    @GetMapping
    public List<Horta> listarTodas() {
        return hortaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horta> buscarPorId(@PathVariable Long id) {
        return hortaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Horta criar(@RequestBody Horta horta) {
        return hortaService.salvar(horta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Horta> atualizar(@PathVariable Long id, @RequestBody Horta horta) {
        try {
            Horta atualizada = hortaService.atualizar(id, horta);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        hortaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Horta> alterarStatus(@PathVariable Long id, @RequestParam StatusHorta status) {
        try {
            Horta horta = hortaService.alterarStatus(id, status);
            return ResponseEntity.ok(horta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
