package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.CursoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.InscricaoCursoRepository;
import com.flordacidade.api.flor_da_cidade_api.repository.UsuarioRepository;
import com.flordacidade.api.flor_da_cidade_api.exception.ResourceNotFoundException;
import com.flordacidade.api.flor_da_cidade_api.exception.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
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

    @Transactional
    public InscricaoCursoModel salvarInscricao(Integer idCurso, Integer idUsuario) {
        // 1. Busca as entidades principais ou lança exceção se não existirem
        CursoModel curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com o ID: " + idCurso));

        // Valida se o usuário existe (boa prática)
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o ID: " + idUsuario);
        }

        // 2. Valida se o curso está ativo
        if (!curso.getAtivo()) {
            throw new BusinessException("Não é possível se inscrever em um curso inativo.");
        }

        // 3. Valida o período de inscrição
        LocalDate hoje = LocalDate.now();
        if (hoje.isBefore(curso.getDataInscInicio()) || hoje.isAfter(curso.getDataInscFim())) {
            throw new BusinessException("Inscrições para este curso não estão abertas no momento. Período: "
                    + curso.getDataInscInicio() + " a " + curso.getDataInscFim());
        }

        // 4. Valida se o usuário já está inscrito
        if (inscricaoRepository.existsByIdCursoAndIdUsuario(idCurso, idUsuario)) {
            throw new BusinessException("Este usuário já está inscrito neste curso.");
        }

        // 5. VALIDAÇÃO DE VAGAS
        long numeroDeInscritos = inscricaoRepository.countByIdCurso(idCurso);
        if (numeroDeInscritos >= curso.getMaxPessoas()) {
            throw new BusinessException("Não há mais vagas disponíveis para este curso. Limite de "
                    + curso.getMaxPessoas() + " vagas atingido.");
        }

        // Se todas as validações passaram, cria a nova inscrição
        InscricaoCursoModel novaInscricao = new InscricaoCursoModel();
        novaInscricao.setIdCurso(idCurso);
        novaInscricao.setIdUsuario(idUsuario);
        // A data é definida automaticamente na entidade

        return inscricaoRepository.save(novaInscricao);
    }

    public void deletar(Integer id) {
        inscricaoRepository.deleteById(id);
    }

    public InscricaoCursoModel atualizar(Integer id, InscricaoCursoModel atualizada) {
        return inscricaoRepository.findById(id).map(inscricao -> {
            inscricao.setIdUsuario(atualizada.getIdUsuario());
            inscricao.setIdCurso(atualizada.getIdCurso());
            return inscricaoRepository.save(inscricao);
        }).orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
    }
}
