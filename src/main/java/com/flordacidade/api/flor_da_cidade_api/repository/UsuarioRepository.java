// src/main/java/com/flordacidade/api/flor_da_cidade_api/repository/UsuarioRepository.java
package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.PessoaModel;
import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {

    Optional<UsuarioModel> findByPessoa(PessoaModel pessoa);

    Optional<UsuarioModel> findByResetPasswordToken(String token);
}
