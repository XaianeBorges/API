package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.service.TecnicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final TecnicoService tecnicoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String matricula = credentials.get("matricula");
        String senha = credentials.get("senha");

        Optional<TecnicoModel> tecnicoOptional = tecnicoService.authenticate(matricula, senha);

        if (tecnicoOptional.isPresent()) {
            TecnicoModel tecnicoAutenticado = tecnicoOptional.get();

            // Cria um mapa para a resposta, evitando expor a senha no JSON.
            Map<String, Object> response = Map.of(
                    "idTecnico", tecnicoAutenticado.getIdTecnico(),
                    "nome", tecnicoAutenticado.getNome(),
                    "matricula", tecnicoAutenticado.getMatricula()
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Matrícula ou senha inválidos.");
        }
    }
}