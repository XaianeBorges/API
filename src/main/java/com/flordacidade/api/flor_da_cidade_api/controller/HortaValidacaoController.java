package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.HortaValidacao;
import com.flordacidade.api.flor_da_cidade_api.model.Horta.StatusHorta;
import com.flordacidade.api.flor_da_cidade_api.service.HortaValidacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horta/validacoes")
public class HortaValidacaoController {

    private final HortaValidacaoService validacaoService;

    public HortaValidacaoController(HortaValidacaoService validacaoService) {
        this.validacaoService = validacaoService;
    }

    @GetMapping
    public List<HortaValidacao> listarTodas() {
        return validacaoService.listarTodas();
    }

    @PostMapping
    public HortaValidacao validar(
            @RequestBody HortaValidacao validacao,
            @RequestParam StatusHorta status) {
        return validacaoService.validarHorta(validacao, status);
    }
}
