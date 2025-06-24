package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<TecnicoModel, Integer> {

    // Usado pelo seu AuthController atual
    Optional<TecnicoModel> findByMatriculaAndSenha(String matricula, String senha);

    // Necessário se você fosse implementar hashing de senha no futuro
    Optional<TecnicoModel> findByMatricula(String matricula);
}