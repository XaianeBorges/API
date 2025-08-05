package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscricaoCursoRepository extends JpaRepository<InscricaoCursoModel, Integer> {

    long countByCurso(CursoModel curso);

    boolean existsByCursoAndUsuario(CursoModel curso, UsuarioModel usuario);
}
