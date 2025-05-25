package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.AreaClassificacao;
import com.flordacidade.api.flor_da_cidade_api.service.AreaClassificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hortas/areas-classificacao")

public class AreaClassificacaoController {
    @Autowired
    private AreaClassificacaoService service;

    @GetMapping
    public List<AreaClassificacao> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public AreaClassificacao buscar(@PathVariable Integer id) {
        return service.buscarPorId(id).orElseThrow(() -> new RuntimeException("Área não encontrada"));
    }

    @PostMapping
    public AreaClassificacao criar(@RequestBody AreaClassificacao a) {
        return service.salvar(a);
    }

    @PutMapping("/{id}")
    public AreaClassificacao atualizar(@PathVariable Integer id, @RequestBody AreaClassificacao a) {
        return service.buscarPorId(id).map(orig -> {
            orig.setNome(a.getNome());
            return service.salvar(orig);
        }).orElseThrow(() -> new RuntimeException("Área não encontrada"));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
