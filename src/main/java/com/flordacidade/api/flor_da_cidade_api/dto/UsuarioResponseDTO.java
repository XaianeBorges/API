package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.UsuarioModel.Escolaridade;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String nome;
    private String cpf;
    private String email;
    private String endereco;
    private String telefone;
    private LocalDate dataNascimento;
    private Escolaridade escolaridade;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
