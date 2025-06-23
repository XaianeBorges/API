package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final FileStorageService fileStorageService;

    public List<CursoModel> listarTodos() {
        return cursoRepository.findAll();
    }

    public Optional<CursoModel> buscarPorId(Integer id) {
        return cursoRepository.findById(id);
    }

    @Transactional
    public CursoModel salvar(CursoModel curso, MultipartFile bannerFile) {
        if (bannerFile != null && !bannerFile.isEmpty()) {
            String fileName = fileStorageService.storeBannerImage(bannerFile);
            curso.setFotoBanner(fileName);
        } else {
            // Se nenhum banner for fornecido, usa o placeholder
            curso.setFotoBanner(PLACEHOLDER_BANNER_FILENAME);
        }
        return cursoRepository.save(curso);
    }

    @Transactional
    public CursoModel atualizar(Integer id, CursoModel cursoDetails, MultipartFile bannerFile) {
        CursoModel existingCurso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o id: " + id));

        String bannerAntigo = existingCurso.getFotoBanner();

        if (bannerFile != null && !bannerFile.isEmpty()) {
            // Se o banner antigo não era o placeholder e existia, deleta-o
            if (bannerAntigo != null && !bannerAntigo.isBlank() && !PLACEHOLDER_BANNER_FILENAME.equals(bannerAntigo)) {
                fileStorageService.deleteBannerImage(bannerAntigo);
            }
            String newFileName = fileStorageService.storeBannerImage(bannerFile);
            existingCurso.setFotoBanner(newFileName);
        }
        // Similar ao HortaService, se bannerFile for nulo, o banner existente é mantido.
        // Para permitir a remoção explícita do banner (voltando para placeholder),
        // seria necessária uma lógica adicional baseada em um sinal do DTO/frontend.


        // Atualiza todos os outros campos do curso
        existingCurso.setNome(cursoDetails.getNome());
        existingCurso.setTipoAtividade(cursoDetails.getTipoAtividade()); // Verifique se este é o getter correto
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

        return cursoRepository.save(existingCurso);
    }

    @Transactional
    public void deletar(Integer id) {
        CursoModel cursoParaDeletar = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o id: " + id + ". Deleção não realizada."));

        String bannerDoCurso = cursoParaDeletar.getFotoBanner();
        cursoRepository.delete(cursoParaDeletar);

        // Deleta o banner físico APENAS se não for o placeholder e se existir
        if (bannerDoCurso != null && !bannerDoCurso.isBlank() && !PLACEHOLDER_BANNER_FILENAME.equals(bannerDoCurso)) {
            fileStorageService.deleteBannerImage(bannerDoCurso);
        }
    }
}