package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HortaRepository extends JpaRepository<Horta, Integer> {

        @Query("SELECT h FROM Horta h " +
                        "JOIN FETCH h.tipoDeHorta th " +
                        "JOIN FETCH h.usuario u " +
                        "WHERE h.statusHorta = :status")
        List<Horta> findByStatusHortaFetchingDetails(@Param("status") Horta.StatusHorta status);

        // NOVO MÉTODO para buscar Horta por ID com todos os detalhes necessários para a
        // tela de descrição
        @Query("SELECT h FROM Horta h " +
           "LEFT JOIN FETCH h.usuario u " +
           "LEFT JOIN FETCH h.tipoDeHorta th " +
           "LEFT JOIN FETCH h.areaClassificacao ac " +
           "LEFT JOIN FETCH h.atividadesProdutivas ap " +
           "LEFT JOIN FETCH h.unidadeEnsino ue " +
           "WHERE h.idHorta = :id")
        @Override
        Optional<Horta> findById(@Param("id") Integer id);

        @Query("SELECT DISTINCT h FROM Horta h " +
           "LEFT JOIN FETCH h.usuario u " +
           "LEFT JOIN FETCH h.tipoDeHorta th " +
           "LEFT JOIN FETCH h.areaClassificacao ac " +
           "LEFT JOIN FETCH h.atividadesProdutivas ap " +
           "LEFT JOIN FETCH h.unidadeEnsino ue")
        List<Horta> findAllFetchingAllDetails();
}