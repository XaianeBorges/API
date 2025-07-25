// src/main/java/com/flordacidade/api/flor_da_cidade_api/repository/UsuarioRepository.java
package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByTelefone(String telefone);

    boolean existsByCpfAndIdUsuarioNot(String cpf, Integer id);

    boolean existsByEmailAndIdUsuarioNot(String email, Integer id);

    boolean existsByTelefoneAndIdUsuarioNot(String telefone, Integer id);

    Optional<UsuarioModel> findByEmail(String email);

}
