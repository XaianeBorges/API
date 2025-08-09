package com.flordacidade.api.flor_da_cidade_api.dto;

import com.flordacidade.api.flor_da_cidade_api.model.TecnicoModel.TecnicoStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TecnicoUpdateDTO {

    private String matricula;
    private String nome;

    @Email
    private String email;

    @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres.")
    private String senha;

    private TecnicoStatus status;
    
    private Integer idRegiao;

    private Boolean adm;
}
