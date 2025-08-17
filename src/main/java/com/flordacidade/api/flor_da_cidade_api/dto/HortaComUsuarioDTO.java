package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HortaComUsuarioDTO {
    // Dados da Horta
    private Integer idHorta;
    private String nomeHorta;
    private String statusHorta;
    private String endereco;
    private LocalDateTime dataCriacao;

    // Dados do Usuário que solicitou
    private String nomeUsuario;
    private String emailUsuario;
    private String telefoneUsuario;
}
