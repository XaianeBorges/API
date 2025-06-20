package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<CursoModel> listarTodos() {
        return cursoRepository.findAll();
    }

    public Optional<CursoModel> buscarPorId(Integer id) {
        return cursoRepository.findById(id);
    }

    // Método salvar modificado para aceitar um arquivo
    public CursoModel salvar(CursoModel curso, MultipartFile fotoBannerFile) {
        // 1. Salva o arquivo no disco e obtém o nome do arquivo gerado
        if (fotoBannerFile != null && !fotoBannerFile.isEmpty()) {
            String fileName = fileStorageService.storeFile(fotoBannerFile);
            // 2. Define o nome do arquivo no modelo antes de salvar no banco
            curso.setFotoBanner(fileName);
        }

        // 3. Salva a entidade Curso no banco de dados
        return cursoRepository.save(curso);
    }

    public void deletar(Integer id) {
        // Idealmente, você também deveria deletar o arquivo de imagem associado aqui.
        cursoRepository.deleteById(id);
    }

    // O método de atualização também precisaria de lógica similar para tratar a
    // imagem
    public CursoModel atualizar(Integer id, CursoModel cursoAtualizado) {
        return cursoRepository.findById(id).map(curso -> {
            cursoAtualizado.setIdCurso(id);
            // Lógica para atualizar a imagem aqui, se uma nova for enviada
            return cursoRepository.save(cursoAtualizado);
        }).orElseThrow(() -> new RuntimeException("Curso não encontrado"));
    }
}