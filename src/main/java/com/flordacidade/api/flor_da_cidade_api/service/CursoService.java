package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.dto.CursoRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.dto.CursoUpdateDTO;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.mapper.CursoMapper;
import com.flordacidade.api.flor_da_cidade_api.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CursoService {

    private static final String PLACEHOLDER_BANNER_FILENAME = "folhin.png";

    private final CursoRepository cursoRepository;
    private final ArquivoService fileStorageService;

    @Autowired
    private CursoMapper cursoMapper;

    public List<CursoModel> listarTodos() {
        return cursoRepository.findAll();
    }

    public Optional<CursoModel> buscarPorId(Integer id) {
        return cursoRepository.findById(id);
    }

    public List<CursoModel> listarPorStatus(CursoModel.Status status) {
        return (List<CursoModel>) cursoRepository.findByStatus(status);
    }

    public List<CursoModel> listarArquivados() {
        return cursoRepository.findByStatus(CursoModel.Status.ARQUIVADO);
    }

    @Transactional
    public CursoModel salvar(CursoRequestDTO cursoDTO, MultipartFile bannerFile) {
        CursoModel novoCurso = cursoMapper.requestDtoToEntity(cursoDTO);

        if (bannerFile != null && !bannerFile.isEmpty()) {
            String fileName = fileStorageService.storeBannerImage(bannerFile);
            novoCurso.setFotoBanner(fileName);
        } else {

            novoCurso.setFotoBanner(PLACEHOLDER_BANNER_FILENAME);
        }
        return cursoRepository.save(novoCurso);
    }

    @Transactional
    public CursoModel atualizar(Integer id, CursoUpdateDTO cursoUpdateDTO, MultipartFile bannerFile) {
        CursoModel existingCurso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o id: " + id));

        String bannerAntigo = existingCurso.getFotoBanner();

        cursoMapper.updateEntityFromDto(cursoUpdateDTO, existingCurso);

        if (bannerFile != null && !bannerFile.isEmpty()) {

            String newFileName = fileStorageService.storeBannerImage(bannerFile);
            existingCurso.setFotoBanner(newFileName);

            if (bannerAntigo != null &&
                    !bannerAntigo.equals(PLACEHOLDER_BANNER_FILENAME) &&
                    !bannerAntigo.equals(newFileName)) {
                fileStorageService.deleteBannerImage(bannerAntigo);
            }
        }
        return cursoRepository.save(existingCurso);
    }

    @Transactional
    public void deletar(Integer id) {
        CursoModel cursoParaDeletar = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id: " + id));

        if (cursoParaDeletar.getFotoBanner() != null &&
                !cursoParaDeletar.getFotoBanner().equals(PLACEHOLDER_BANNER_FILENAME)) {
            fileStorageService.deleteBannerImage(cursoParaDeletar.getFotoBanner());
        }

        cursoRepository.delete(cursoParaDeletar);
    }
}