package com.flordacidade.api.flor_da_cidade_api.repository;

import java.util.List;

import com.flordacidade.api.flor_da_cidade_api.model.CursoModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<CursoModel, Integer> {

    List<CursoModel> findByStatus(CursoModel.Status status);
}