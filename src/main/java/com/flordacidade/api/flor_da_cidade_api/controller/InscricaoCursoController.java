package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import com.flordacidade.api.flor_da_cidade_api.service.InscricaoCursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoCursoController {

    @Autowired
    private InscricaoCursoService service;

    @GetMapping
    public List<InscricaoCursoModel> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscricaoCursoModel> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InscricaoCursoModel> salvar(@RequestBody InscricaoCursoModel inscricao) {
        return ResponseEntity.ok(service.salvar(inscricao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InscricaoCursoModel> atualizar(@PathVariable Integer id,
            @RequestBody InscricaoCursoModel atualizada) {
        return ResponseEntity.ok(service.atualizar(id, atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
