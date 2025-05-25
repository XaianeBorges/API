package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.InscricaoCursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InscricaoCursoService {

    @Autowired
    private InscricaoCursoRepository repository;

    public List<InscricaoCursoModel> listarTodos() {
        return repository.findAll();
    }

    public Optional<InscricaoCursoModel> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public InscricaoCursoModel salvar(InscricaoCursoModel inscricao) {
        return repository.save(inscricao);
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }

    public InscricaoCursoModel atualizar(Integer id, InscricaoCursoModel atualizada) {
        return repository.findById(id).map(inscricao -> {
            inscricao.setIdUsuario(atualizada.getIdUsuario());
            inscricao.setIdCurso(atualizada.getIdCurso());
            return repository.save(inscricao);
        }).orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
    }
}
