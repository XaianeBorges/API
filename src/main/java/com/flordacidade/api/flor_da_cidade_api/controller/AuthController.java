package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.service.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private final TecnicoService tecnicoService;

    @Autowired
    public AuthController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String matricula = body.get("matricula");
        String senha = body.get("senha");

        Optional<TecnicoModel> tecnico = tecnicoService.authenticate(matricula, senha);

        if (tecnico.isPresent()) {
            return ResponseEntity.ok(tecnico.get());
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Usuário ou senha inválidos"));
        }
    }
}
