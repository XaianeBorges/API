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

    private String limparNumeros(String valor) {
        if (valor == null) {
            return null;
        }
        return valor.replaceAll("[^0-9]", ""); // Remove tudo que não for um dígito
    }

    @Transactional
    public PessoaModel criar(PessoaModel p) {

        // Limpa os dados antes da validação de unicidade
        String cpfLimpo = limparNumeros(p.getCpf());
        String telefoneLimpo = limparNumeros(p.getTelefone());

        p.setCpf(cpfLimpo);
        p.setTelefone(telefoneLimpo);

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

        String cpfLimpo = limparNumeros(pAtual.getCpf());
        String telefoneLimpo = limparNumeros(pAtual.getTelefone());

        pAtual.setCpf(cpfLimpo);
        pAtual.setTelefone(telefoneLimpo);

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
