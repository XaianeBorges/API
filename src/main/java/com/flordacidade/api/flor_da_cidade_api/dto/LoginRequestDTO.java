package com.flordacidade.api.flor_da_cidade_api.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String matricula;
    private String senha;
}
