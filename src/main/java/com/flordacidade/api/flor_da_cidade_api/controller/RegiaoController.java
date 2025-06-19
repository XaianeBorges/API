package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.RegiaoModel;
import com.flordacidade.api.flor_da_cidade_api.service.RegiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/regioes")
public class RegiaoController {

    private final RegiaoService regiaoService;

    @Autowired
    public RegiaoController(RegiaoService regiaoService) {
        this.regiaoService = regiaoService;
    }

    @GetMapping
    public ResponseEntity<List<RegiaoModel>> listarTodas() {
        return ResponseEntity.ok(regiaoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegiaoModel> buscarPorId(@PathVariable Integer id) {
        return regiaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RegiaoModel> criar(@RequestBody RegiaoModel regiao) {
        return ResponseEntity.ok(regiaoService.salvar(regiao));
    }
    
    // Você pode adicionar endpoints de PUT (update) e DELETE se necessário
}