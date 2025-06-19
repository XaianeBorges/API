package com.flordacidade.api.flor_da_cidade_api.repository;

import com.flordacidade.api.flor_da_cidade_api.model.RegiaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegiaoRepository extends JpaRepository<RegiaoModel, Integer> {
    // O JpaRepository já fornece métodos como findAll(), findById(), save(), deleteById(), etc.
    // Você pode adicionar métodos de busca personalizados aqui se precisar, por exemplo:
    // Optional<RegiaoModel> findByNome(String nome);
}