// src/main/java/com/flordacidade/api/flor_da_cidade_api/model/Escolaridade.java
package com.flordacidade.api.flor_da_cidade_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public enum Escolaridade {
    SEM_ESCOLARIDADE,
    ENSINO_FUNDAMENTAL_INCOMPLETO,
    ENSINO_FUNDAMENTAL_COMPLETO,
    ENSINO_MEDIO_INCOMPLETO,
    ENSINO_MEDIO_COMPLETO,
    ENSINO_SUPERIOR_INCOMPLETO,
    ENSINO_SUPERIOR_COMPLETO,
    POS_GRADUACAO;
}
