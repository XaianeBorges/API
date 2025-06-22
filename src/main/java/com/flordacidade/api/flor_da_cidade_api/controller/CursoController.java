package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping("/opcoes")
    public ResponseEntity<Map<String, List<String>>> getFormOptions() {
        Map<String, List<String>> options = Map.of(
                "tiposAtividade", Arrays.stream(CursoModel.TipoAtividade.values()).map(Enum::name).collect(Collectors.toList()),
                "publicosAlvo", Arrays.stream(CursoModel.PublicoAlvo.values()).map(Enum::name).collect(Collectors.toList()),
                "turnos", Arrays.stream(CursoModel.Turno.values()).map(Enum::name).collect(Collectors.toList())
        );
        return ResponseEntity.ok(options);
    }

    @GetMapping
    public ResponseEntity<List<CursoModel>> listarTodos() {
        return ResponseEntity.ok(cursoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoModel> buscarPorId(@PathVariable Integer id) {
        return cursoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CursoModel> criar(
            @RequestPart("curso") CursoModel curso,
            @RequestPart(value = "banner", required = false) MultipartFile bannerFile) {
        CursoModel cursoSalvo = cursoService.salvar(curso, bannerFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoSalvo);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<CursoModel> atualizar(
            @PathVariable Integer id,
            @RequestPart("curso") CursoModel cursoDetails,
            @RequestPart(value = "banner", required = false) MultipartFile bannerFile) {
        CursoModel cursoAtualizado = cursoService.atualizar(id, cursoDetails, bannerFile);
        return ResponseEntity.ok(cursoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        cursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}