package com.flordacidade.api.flor_da_cidade_api.controller;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.CursoRepository;
import com.flordacidade.api.flor_da_cidade_api.model.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/imagem_cursos")
@RequiredArgsConstructor
public class CursoImagemController {

    private final CursoRepository cursoRepository;

    @Value("${upload.curso.dir:uploads/cursos}")
    private String cursoUploadDir;

    @PostMapping("/{id}/foto-banner")
    public ResponseEntity<String> uploadFotoBanner(@PathVariable Integer id,
                                                   @RequestParam("arquivo") MultipartFile arquivo) {
        if (arquivo.isEmpty()) {
            return ResponseEntity.badRequest().body("Arquivo está vazio");
        }

        CursoModel curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado: " + id));

        try {
            Path pasta = Paths.get(cursoUploadDir);
            if (!Files.exists(pasta)) {
                Files.createDirectories(pasta);
            }

            String nomeOriginal = StringUtils.cleanPath(arquivo.getOriginalFilename());
            String extensao = nomeOriginal.contains(".") ? nomeOriginal.substring(nomeOriginal.lastIndexOf('.')) : "";
            String nomeArquivo = UUID.randomUUID() + extensao;

            Path caminhoArquivo = pasta.resolve(nomeArquivo);
            Files.copy(arquivo.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);

            curso.setFotoBanner(nomeArquivo);
            cursoRepository.save(curso);

            return ResponseEntity.ok("Banner salvo com sucesso: " + nomeArquivo);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar imagem: " + e.getMessage());
        }
    }
}
//thjhrjhlsxtyhjnç
{System.out.println("só Jesus")}