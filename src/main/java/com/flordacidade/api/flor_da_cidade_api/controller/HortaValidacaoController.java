package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.Horta.StatusHorta;
import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import com.flordacidade.api.flor_da_cidade_api.service.HortaValidacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horta/validacoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HortaValidacaoController {

    private final HortaValidacaoService validacaoService;

    @GetMapping
    public ResponseEntity<List<HortaValidacao>> listarTodas() {
        List<HortaValidacao> validacoes = validacaoService.listarTodas();
        return ResponseEntity.ok(validacoes);
    }

    @PostMapping
    public ResponseEntity<HortaValidacao> validar(
            @RequestBody HortaValidacao validacao,
            @RequestParam("status") StatusHorta status) {
        // Esta chamada não dará mais erro
        HortaValidacao novaValidacao = validacaoService.validarHorta(validacao, status);
        return new ResponseEntity<>(novaValidacao, HttpStatus.CREATED);
    }
}