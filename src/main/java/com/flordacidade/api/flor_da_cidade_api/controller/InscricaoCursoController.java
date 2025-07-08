package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import com.flordacidade.api.flor_da_cidade_api.service.InscricaoCursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curos/inscricoes")
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
    public ResponseEntity<InscricaoCursoModel> criarInscricao(
            @RequestParam("idCurso") Integer idCurso,
            @RequestParam("idUsuario") Integer idUsuario) {

        // Chama o novo método do serviço que contém a lógica de negócio
        InscricaoCursoModel inscricaoSalva = service.salvarInscricao(idCurso, idUsuario);

        // Retorna 201 Created, que é o status correto para criação de recurso
        return new ResponseEntity<>(inscricaoSalva, HttpStatus.CREATED);
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
