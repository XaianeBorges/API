package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.Horta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Certifique-se que este import está presente
import org.springframework.stereotype.Repository;

import java.util.List; // Certifique-se que este import está presente

@Repository
public interface HortaRepository extends JpaRepository<Horta, Integer> {

    @Query("SELECT h FROM Horta h " +
            "JOIN FETCH h.tipoDeHorta th " +   // Alias 'th' para tipoDeHorta
            "JOIN FETCH h.usuario u " +        // Alias 'u' para usuario
            "JOIN FETCH u.pessoa p " +         // Alias 'p' para pessoa
            "WHERE h.statusHorta = :status")
    List<Horta> findByStatusHortaFetchingDetails(@Param("status") Horta.StatusHorta status);

}