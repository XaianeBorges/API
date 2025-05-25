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

    private final String label;

    Escolaridade(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Escolaridade fromLabel(String label) {
        if (label == null) return null;
        String trimmed = label.trim();
        for (Escolaridade e : values()) {
            if (e.label.equalsIgnoreCase(trimmed) || e.name().equalsIgnoreCase(trimmed)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Escolaridade inválida: " + label);
    }
}