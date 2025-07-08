package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.InscricaoCursoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscricaoCursoRepository extends JpaRepository<InscricaoCursoModel, Integer> {

    /**
     * Conta o número de inscrições para um determinado ID de curso.
     * Como não temos status, contamos todas as linhas.
     * 
     * @param idCurso O ID do curso.
     * @return O número de inscrições encontradas para o curso.
     */
    long countByIdCurso(Integer idCurso);

    /**
     * Verifica se já existe uma inscrição para a combinação de ID de curso e ID de
     * usuário.
     *
     * @param idCurso   O ID do curso.
     * @param idUsuario O ID do usuário.
     * @return true se a inscrição já existe, false caso contrário.
     */
    boolean existsByIdCursoAndIdUsuario(Integer idCurso, Integer idUsuario);
}
