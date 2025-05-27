package com.flordacidade.api.flor_da_cidade_api.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EscolaridadeConverter implements AttributeConverter<Escolaridade, String> {

    @Override
    public String convertToDatabaseColumn(Escolaridade attribute) {
        return attribute == null ? null : attribute.getValor();
    }

    @Override
    public Escolaridade convertToEntityAttribute(String dbData) {
        return dbData == null
                ? null
                : Escolaridade.fromString(dbData);
    }
}
