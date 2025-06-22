package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.service.CursoService;
import com.flordacidade.api.flor_da_cidade_api.service.ExcelExportService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    @Autowired
    private CursoService cursoService;
    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/opcoes")
    public ResponseEntity<Map<String, List<String>>> getFormOptions() {
        Map<String, List<String>> options = Map.of(
                "tiposAtividade",
                Arrays.stream(CursoModel.TipoAtividade.values()).map(Enum::name).collect(Collectors.toList()),
                "publicosAlvo",
                Arrays.stream(CursoModel.PublicoAlvo.values()).map(Enum::name).collect(Collectors.toList()),
                "turnos", Arrays.stream(CursoModel.Turno.values()).map(Enum::name).collect(Collectors.toList()));
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

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<CursoModel> criar(
            @RequestPart("curso") CursoModel curso,
            @RequestPart(value = "banner", required = false) MultipartFile bannerFile) {
        CursoModel cursoSalvo = cursoService.salvar(curso, bannerFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoSalvo);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
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

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> exportarCursosAtivosParaExcel() throws IOException {

        // 1. Busca todos os cursos
        List<CursoModel> todosOsCursos = cursoService.listarTodos();

        // 2. Filtra APENAS os cursos ativos, como o nome do botão no front-end sugere
        List<CursoModel> cursosAtivos = todosOsCursos.stream()
                .filter(curso -> curso.getAtivo())
                .collect(Collectors.toList());

        // 3. Gera o arquivo Excel com a lista filtrada
        ByteArrayInputStream bais = excelExportService.exportarCursosParaExcel(cursosAtivos);

        // 4. Monta a resposta HTTP para o download
        HttpHeaders headers = new HttpHeaders();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        // O nome do arquivo no back-end deve ter a extensão correta (.xlsx)
        String filename = "relatorio-cursos-ativos-" + timestamp + ".xlsx";

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(bais));
    }
}
