package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;
import com.flordacidade.api.flor_da_cidade_api.repository.CursoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.InscricaoCursoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.dto.InscricaoRequestDTO;
import com.flordacidade.api.flor_da_cidade_api.exception.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InscricaoCursoService {

    @Autowired
    private InscricaoCursoRepository inscricaoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<InscricaoCursoModel> listarTodos() {
        return inscricaoRepository.findAll();
    }

    public Optional<InscricaoCursoModel> buscarPorId(Integer id) {
        return inscricaoRepository.findById(id);
    }

    public List<InscricaoCursoModel> listarPorCursoId(Integer cursoId) {

        if (!cursoRepository.existsById(cursoId)) {
            throw new ResourceNotFoundException("Curso não encontrado com o ID: " + cursoId);
        }
        return inscricaoRepository.findByCursoIdWithDetails(cursoId);
    }

    @Transactional
    public InscricaoCursoModel salvarInscricao(InscricaoRequestDTO requestDTO) {

        CursoModel curso = cursoRepository.findById(requestDTO.getIdCurso())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Curso não encontrado com o ID: " + requestDTO.getIdCurso()));

        UsuarioModel usuario = usuarioRepository.findById(requestDTO.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com o ID: " + requestDTO.getIdUsuario()));

        if (curso.getStatus() != CursoModel.Status.ATIVO) {
            throw new BusinessException("Não é possível se inscrever em um curso inativo.");
        }

        LocalDate hoje = LocalDate.now();
        if (hoje.isBefore(curso.getDataInscInicio()) || hoje.isAfter(curso.getDataInscFim())) {
            throw new BusinessException("Inscrições para este curso não estão abertas no momento. Período: "
                    + curso.getDataInscInicio() + " a " + curso.getDataInscFim());
        }

        if (inscricaoRepository.existsByCursoAndUsuario(curso, usuario)) {
            throw new BusinessException("Este usuário já está inscrito neste curso.");
        }
        long numeroDeInscritos = inscricaoRepository.countByCurso(curso);
        if (numeroDeInscritos >= curso.getMaxPessoas()) {
            throw new BusinessException("Não há mais vagas disponíveis para este curso.");
        }

        InscricaoCursoModel novaInscricao = new InscricaoCursoModel();
        novaInscricao.setCurso(curso);
        novaInscricao.setUsuario(usuario);

        return inscricaoRepository.save(novaInscricao);
    }

    public void deletar(Integer id) {
        inscricaoRepository.deleteById(id);
    }

}
