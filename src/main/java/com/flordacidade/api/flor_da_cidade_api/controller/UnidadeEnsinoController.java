package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.UnidadeEnsino;
import com.flordacidade.api.flor_da_cidade_api.service.UnidadeEnsinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hortas/unidade-ensino")

public class UnidadeEnsinoController {
    @Autowired
    private UnidadeEnsinoService service;

    @GetMapping
    public List<UnidadeEnsino> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public UnidadeEnsino buscar(@PathVariable Integer id) {
        return service.buscarPorId(id).orElseThrow(() -> new RuntimeException("Unidade de ensino não encontrado"));
    }

    @PostMapping
    public UnidadeEnsino criar(@RequestBody UnidadeEnsino a) {
        return service.salvar(a);
    }

    @PutMapping("/{id}")
    public UnidadeEnsino atualizar(@PathVariable Integer id, @RequestBody UnidadeEnsino a) {
        return service.buscarPorId(id).map(orig -> {
            orig.setNome(a.getNome());
            return service.salvar(orig);
        }).orElseThrow(() -> new RuntimeException("Unidade de ensino não encontrado"));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
