package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import com.flordacidade.api.flor_da_cidade_api.service.HortaValidacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validacoes")
@RequiredArgsConstructor
public class HortaValidacaoController {

    private final HortaValidacaoService service;

    @PostMapping
    public ResponseEntity<HortaValidacao> validar(@RequestBody HortaValidacao validacao) {
        return ResponseEntity.ok(service.validarHorta(validacao));
    }
}

