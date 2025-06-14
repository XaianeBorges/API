// src/main/java/com/flordacidade/api/flor_da_cidade_api/model/TecnicoStatus.java
package com.flordacidade.api.flor_da_cidade_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public enum TecnicoStatus {
    ATIVO,
    INATIVO;
}