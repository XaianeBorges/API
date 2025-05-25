package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.AtividadesProdutivas;
import com.flordacidade.api.flor_da_cidade_api.service.AtividadesProdutivasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hortas/atividades-produtivas")

public class AtividadesProdutivasController {
    @Autowired
    private AtividadesProdutivasService service;

    @GetMapping
    public List<AtividadesProdutivas> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public AtividadesProdutivas buscar(@PathVariable Integer id) {
        return service.buscarPorId(id).orElseThrow(() -> new RuntimeException("Atividade não encontrada"));
    }

    @PostMapping
    public AtividadesProdutivas criar(@RequestBody AtividadesProdutivas a) {
        return service.salvar(a);
    }

    @PutMapping("/{id}")
    public AtividadesProdutivas atualizar(@PathVariable Integer id, @RequestBody AtividadesProdutivas a) {
        return service.buscarPorId(id).map(orig -> {
            orig.setNome(a.getNome());
            return service.salvar(orig);
        }).orElseThrow(() -> new RuntimeException("Atividade não encontrada"));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
