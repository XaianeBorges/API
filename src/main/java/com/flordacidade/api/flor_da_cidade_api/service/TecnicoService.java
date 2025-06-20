package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import com.flordacidade.api.flor_da_cidade_api.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    private final TecnicoRepository repository;

    @Autowired
    public TecnicoService(TecnicoRepository repository) {
        this.repository = repository;
    }

    // --- Métodos CRUD ---

    public List<TecnicoModel> getAll() {
        return repository.findAll();
    }

    public Optional<TecnicoModel> getById(Integer id) {
        return repository.findById(id);
    }

    public TecnicoModel create(TecnicoModel tecnico) {
        return repository.save(tecnico);
    }

    public Optional<TecnicoModel> update(Integer id, TecnicoModel tecnicoDetails) {
        return repository.findById(id)
                .map(existingTecnico -> {
                    existingTecnico.setNome(tecnicoDetails.getNome());
                    existingTecnico.setMatricula(tecnicoDetails.getMatricula());
                    existingTecnico.setSenha(tecnicoDetails.getSenha());
                    existingTecnico.setStatus(tecnicoDetails.getStatus());
                    existingTecnico.setRegiao(tecnicoDetails.getRegiao());
                    return repository.save(existingTecnico);
                });
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    // --- Método de Autenticação ---

    public Optional<TecnicoModel> authenticate(String matricula, String senha) {
        return repository.findByMatriculaAndSenha(matricula, senha);
    }
}