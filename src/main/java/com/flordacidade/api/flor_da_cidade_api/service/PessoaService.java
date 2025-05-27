// src/main/java/com/flordacidade/api/flor_da_cidade_api/service/PessoaService.java
package com.flordacidade.api.flor_da_cidade_api.service;

import com.flordacidade.api.flor_da_cidade_api.model.PessoaModel;
import com.flordacidade.api.flor_da_cidade_api.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    @Autowired
    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional(readOnly = true)
    public List<PessoaModel> listarTodos() {
        return pessoaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PessoaModel> buscarPorId(Integer id) {
        return pessoaRepository.findById(id);
    }

    @Transactional
    public PessoaModel criar(PessoaModel pessoa) {
        validarUnicidade(pessoa);
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public PessoaModel atualizar(Integer id, PessoaModel pessoaAtualizada) {
        return pessoaRepository.findById(id)
                .map(pessoa -> {
                    validarUnicidadeAtualizacao(id, pessoaAtualizada);
                    atualizarCampos(pessoa, pessoaAtualizada);
                    return pessoaRepository.save(pessoa);
                })
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada com ID: " + id));
    }

    @Transactional
    public void excluir(Integer id) {
        pessoaRepository.deleteById(id);
    }

    private void validarUnicidade(PessoaModel pessoa) {
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        if (pessoaRepository.existsByEmail(pessoa.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        if (pessoaRepository.existsByTelefone(pessoa.getTelefone())) {
            throw new IllegalArgumentException("Telefone já cadastrado");
        }
    }

    private void validarUnicidadeAtualizacao(Integer id, PessoaModel pessoa) {
        if (pessoaRepository.existsByCpfAndIdPessoaNot(pessoa.getCpf(), id)) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
        if (pessoaRepository.existsByEmailAndIdPessoaNot(pessoa.getEmail(), id)) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        if (pessoaRepository.existsByTelefoneAndIdPessoaNot(pessoa.getTelefone(), id)) {
            throw new IllegalArgumentException("Telefone já cadastrado");
        }
    }

    private void atualizarCampos(PessoaModel original, PessoaModel atualizada) {
        original.setNome(atualizada.getNome());
        original.setCpf(atualizada.getCpf());
        original.setDataNascimento(atualizada.getDataNascimento());
        original.setEmail(atualizada.getEmail());
        original.setEndereco(atualizada.getEndereco());
        original.setTelefone(atualizada.getTelefone());
        original.setEscolaridade(atualizada.getEscolaridade());
    }
}
