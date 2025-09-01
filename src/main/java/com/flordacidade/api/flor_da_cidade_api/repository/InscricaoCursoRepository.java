package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscricaoCursoRepository extends JpaRepository<InscricaoCursoModel, Integer> {

    long countByCurso(CursoModel curso);

    boolean existsByCursoAndUsuario(CursoModel curso, UsuarioModel usuario);

    @Query("SELECT i FROM InscricaoCursoModel i JOIN FETCH i.usuario JOIN FETCH i.curso WHERE i.curso.idCurso = :cursoId")
    List<InscricaoCursoModel> findByCursoIdWithDetails(@Param("cursoId") Integer cursoId);
}
