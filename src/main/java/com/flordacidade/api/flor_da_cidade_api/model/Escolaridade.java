package com.flordacidade.api.flor_da_cidade_api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Escolaridade {
    SEM_ESCOLARIDADE("Sem escolaridade"),
    ENSINO_FUNDAMENTAL_INCOMPLETO("Ensino fundamental incompleto"),
    ENSINO_FUNDAMENTAL_COMPLETO("Ensino fundamental completo"),
    ENSINO_MEDIO_INCOMPLETO("Ensino médio incompleto"),
    ENSINO_MEDIO_COMPLETO("Ensino médio completo"),
    ENSINO_SUPERIOR_INCOMPLETO("Ensino superior incompleto"),
    ENSINO_SUPERIOR_COMPLETO("Ensino superior completo"),
    POS_GRADUACAO("Pós graduação");

    private final String valor;

    Escolaridade(String valor) {
        this.valor = valor;
    }

    @JsonValue
    public String getValor() {
        return valor;
    }

    @JsonCreator
    public static Escolaridade fromString(String text) {
        if (text == null) return null;
        for (Escolaridade e : Escolaridade.values()) {
            if (e.valor.equalsIgnoreCase(text.trim())) {
                return e;
            }
        }
        throw new IllegalArgumentException("Escolaridade inválida: " + text);
    }
}
