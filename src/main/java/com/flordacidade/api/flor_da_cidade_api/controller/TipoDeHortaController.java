package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.TipoDeHorta;
import com.flordacidade.api.flor_da_cidade_api.service.TipoDeHortaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hortas/tipo")

public class TipoDeHortaController {
    @Autowired
    private TipoDeHortaService service;

    @GetMapping
    public List<TipoDeHorta> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public TipoDeHorta buscar(@PathVariable Integer id) {
        return service.buscarPorId(id).orElseThrow(() -> new RuntimeException("Tipo de horta não encontrada"));
    }

    @PostMapping
    public TipoDeHorta criar(@RequestBody TipoDeHorta a) {
        return service.salvar(a);
    }

    @PutMapping("/{id}")
    public TipoDeHorta atualizar(@PathVariable Integer id, @RequestBody TipoDeHorta a) {
        return service.buscarPorId(id).map(orig -> {
            orig.setNome(a.getNome());
            return service.salvar(orig);
        }).orElseThrow(() -> new RuntimeException("Tipo de horta não encontrada"));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
