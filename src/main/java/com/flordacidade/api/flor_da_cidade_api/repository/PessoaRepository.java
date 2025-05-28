// src/main/java/com/flordacidade/api/flor_da_cidade_api/repository/PessoaRepository.java
package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.PessoaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaModel, Integer> {

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);
    boolean existsByCpfAndIdPessoaNot(String cpf, Integer id);
    boolean existsByEmailAndIdPessoaNot(String email, Integer id);
    boolean existsByTelefoneAndIdPessoaNot(String telefone, Integer id);
}
