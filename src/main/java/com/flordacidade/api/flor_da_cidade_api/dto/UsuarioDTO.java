package com.flordacidade.api.flor_da_cidade_api.dto;

import jakarta.validation.constraints.*;

public class UsuarioDTO {

    @NotNull
    private Integer pessoaId;

    @NotBlank
    @Size(min = 6, max = 255)
    private String senha;

    private Boolean ativo = true;
    private String matricula;

    // getters & setters

    public Integer getPessoaId() { return pessoaId; }
    public void setPessoaId(Integer pessoaId) { this.pessoaId = pessoaId; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
}
