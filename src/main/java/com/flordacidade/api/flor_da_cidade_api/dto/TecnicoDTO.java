// src/main/java/com/flordacidade/api/flor_da_cidade_api/dto/TecnicoDTO.java
package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.Tecnico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TecnicoDTO {

    @NotNull
    private Tecnico.Status status;

    @NotBlank
    private String matricula;

    @NotBlank
    @Size(min = 6)
    private String senha;

    private String regiao;

    @NotBlank
    @Size(max = 45)
    private String nome;  // ← Novo campo

    // getters & setters

    public Tecnico.Status getStatus() {
        return status;
    }

    public void setStatus(Tecnico.Status status) {
        this.status = status;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}