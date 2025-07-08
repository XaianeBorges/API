package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok anotation para injeção de dependência via construtor para campos final
public class CursoService {

    private static final String PLACEHOLDER_BANNER_FILENAME = "folhin.png";

    private final CursoRepository cursoRepository;
    private final ArquivoService fileStorageService;

    public List<CursoModel> listarTodos() {
        return cursoRepository.findAll();
    }

    public Optional<CursoModel> buscarPorId(Integer id) {
        return cursoRepository.findById(id);
    }

    @Transactional
    public CursoModel salvar(CursoModel curso, MultipartFile bannerFile) {
        if (bannerFile != null && !bannerFile.isEmpty()) {
            // Usa o método específico para salvar banners
            String fileName = fileStorageService.storeBannerImage(bannerFile);
            curso.setFotoBanner(fileName);
        } else {
            // Se nenhum banner for fornecido, usa o placeholder
            curso.setFotoBanner(PLACEHOLDER_BANNER_FILENAME);
        }
        // As datas de criação e atualização são gerenciadas por @PrePersist e
        // @PreUpdate em CursoModel
        return cursoRepository.save(curso);
    }

    @Transactional
    public CursoModel atualizar(Integer id, CursoModel cursoDetails, MultipartFile bannerFile) {
        CursoModel existingCurso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o id: " + id));

        String bannerAntigo = existingCurso.getFotoBanner(); // Guarda o nome do banner antigo

        if (bannerFile != null && !bannerFile.isEmpty()) {
            // CORREÇÃO: Usa o método específico para salvar banners
            String newFileName = fileStorageService.storeBannerImage(bannerFile);
            existingCurso.setFotoBanner(newFileName);

            // Deleta o banner antigo se ele existir, não for o placeholder e for diferente
            // do novo
            if (bannerAntigo != null &&
                    !bannerAntigo.equals(PLACEHOLDER_BANNER_FILENAME) &&
                    !bannerAntigo.equals(newFileName)) {
                fileStorageService.deleteBannerImage(bannerAntigo);
            }
        }

        // Atualiza todos os outros campos do curso
        existingCurso.setNome(cursoDetails.getNome());
        existingCurso.setTipoAtividade(cursoDetails.getTipoAtividade());
        existingCurso.setDescricao(cursoDetails.getDescricao());
        existingCurso.setLocal(cursoDetails.getLocal());
        existingCurso.setInstituicao(cursoDetails.getInstituicao());
        existingCurso.setPublicoAlvo(cursoDetails.getPublicoAlvo());
        existingCurso.setDataInicio(cursoDetails.getDataInicio());
        existingCurso.setDataFim(cursoDetails.getDataFim());
        existingCurso.setDataInscInicio(cursoDetails.getDataInscInicio());
        existingCurso.setDataInscFim(cursoDetails.getDataInscFim());
        existingCurso.setTurno(cursoDetails.getTurno());
        existingCurso.setMaxPessoas(cursoDetails.getMaxPessoas());
        existingCurso.setCargaHoraria(cursoDetails.getCargaHoraria());
        existingCurso.setAtivo(cursoDetails.getAtivo());
        // A data de atualização é gerenciada por @PreUpdate em CursoModel

        return cursoRepository.save(existingCurso);
    }

    @Transactional
    public void deletar(Integer id) {
        CursoModel cursoParaDeletar = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com id: " + id));

        // Deleta o banner associado, a menos que seja o placeholder
        if (cursoParaDeletar.getFotoBanner() != null &&
                !cursoParaDeletar.getFotoBanner().equals(PLACEHOLDER_BANNER_FILENAME)) {
            fileStorageService.deleteBannerImage(cursoParaDeletar.getFotoBanner());
        }

        cursoRepository.delete(cursoParaDeletar);
    }
}