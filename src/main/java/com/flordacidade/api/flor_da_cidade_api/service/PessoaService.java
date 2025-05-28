// src/main/java/com/flordacidade/api/flor_da_cidade_api/service/PessoaService.java
package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.PessoaModel;
import com.flordacidade.api.flor_da_cidade_api.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    private final PessoaRepository repo;

    public PessoaService(PessoaRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<PessoaModel> listarTodos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PessoaModel> buscarPorId(Integer id) {
        return repo.findById(id);
    }

    @Transactional
    public PessoaModel criar(PessoaModel p) {
        if (repo.existsByCpf(p.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        if (repo.existsByEmail(p.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        if (repo.existsByTelefone(p.getTelefone())) {
            throw new IllegalArgumentException("Telefone já cadastrado");
        }
        return repo.save(p);
    }

    @Transactional
    public PessoaModel atualizar(Integer id, PessoaModel pAtual) {
        return repo.findById(id)
                .map(p -> {
                    if (repo.existsByCpfAndIdPessoaNot(pAtual.getCpf(), id)) {
                        throw new IllegalArgumentException("CPF já cadastrado");
                    }
                    if (repo.existsByEmailAndIdPessoaNot(pAtual.getEmail(), id)) {
                        throw new IllegalArgumentException("E-mail já cadastrado");
                    }
                    if (repo.existsByTelefoneAndIdPessoaNot(pAtual.getTelefone(), id)) {
                        throw new IllegalArgumentException("Telefone já cadastrado");
                    }
                    p.setNome(pAtual.getNome());
                    p.setCpf(pAtual.getCpf());
                    p.setDataNascimento(pAtual.getDataNascimento());
                    p.setEmail(pAtual.getEmail());
                    p.setEndereco(pAtual.getEndereco());
                    p.setTelefone(pAtual.getTelefone());
                    p.setEscolaridade(pAtual.getEscolaridade());
                    return repo.save(p);
                })
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada com ID: " + id));
    }

    @Transactional
    public void excluir(Integer id) {
        repo.deleteById(id);
    }
}
