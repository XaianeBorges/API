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
            String fileName = fileStorageService.storeFile(bannerFile);
            curso.setFotoBanner(fileName);
        }
        return cursoRepository.save(curso);
    }

    @Transactional
    public CursoModel atualizar(Integer id, CursoModel cursoDetails, MultipartFile bannerFile) {
        CursoModel existingCurso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o id: " + id));

        // Se um novo banner for enviado, salva o novo e atualiza o nome do arquivo.
        if (bannerFile != null && !bannerFile.isEmpty()) {
            // TODO: Implementar lógica para deletar o arquivo de banner antigo para não acumular lixo.
            String newFileName = fileStorageService.storeFile(bannerFile);
            existingCurso.setFotoBanner(newFileName);
        }

        // Atualiza todos os campos do curso existente com os detalhes recebidos.
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

        return cursoRepository.save(existingCurso);
    }

    @Transactional
    public void deletar(Integer id) {
        // TODO: Implementar lógica para deletar o arquivo de imagem do disco.
        cursoRepository.deleteById(id);
    }
}