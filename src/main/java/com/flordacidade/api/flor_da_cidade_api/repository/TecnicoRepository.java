package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<TecnicoModel, Integer> {

    Optional<TecnicoModel> findByMatriculaAndSenha(String matricula, String senha);
}