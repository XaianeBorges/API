package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.Tecnico;
import jakarta.validation.constraints.*;

public class TecnicoDTO {

    @NotNull
    private Tecnico.Status status;

    @NotBlank
    private String matricula;

    @NotBlank
    @Size(min = 6)
    private String senha;

    private String regiao;

    // getters & setters

    public Tecnico.Status getStatus() { return status; }
    public void setStatus(Tecnico.Status status) { this.status = status; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getRegiao() { return regiao; }
    public void setRegiao(String regiao) { this.regiao = regiao; }
}
