package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<CursoModel> listarTodos() {
        return cursoRepository.findAll();
    }

    public Optional<CursoModel> buscarPorId(Integer id) {
        return cursoRepository.findById(id);
    }

    public CursoModel salvar(CursoModel curso) {
        return cursoRepository.save(curso);
    }

    public void deletar(Integer id) {
        cursoRepository.deleteById(id);
    }

    public CursoModel atualizar(Integer id, CursoModel cursoAtualizado) {
        return cursoRepository.findById(id).map(curso -> {
            cursoAtualizado.setIdCurso(id);
            return cursoRepository.save(cursoAtualizado);
        }).orElseThrow(() -> new RuntimeException("Curso não encontrado"));
    }
}
