package com.flordacidade.api.flor_da_cidade_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public enum PublicoAlvo {
    Geral,
    Interno,
    Comunidade,
    Estudantes,
    Idosos
}
