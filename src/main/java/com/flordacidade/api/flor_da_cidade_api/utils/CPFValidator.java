package com.flordacidade.api.flor_da_cidade_api.utils;

public class CPFValidator {

    public static boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio.");
        }

        cpf = cpf.replaceAll("\\D", ""); // Remove caracteres não numéricos

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        // Cálculo dos dígitos verificadores
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = (sum % 11) < 2 ? 0 : 11 - (sum % 11);

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = (sum % 11) < 2 ? 0 : 11 - (sum % 11);

        if (firstDigit != Character.getNumericValue(cpf.charAt(9)) ||
                secondDigit != Character.getNumericValue(cpf.charAt(10))) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        return true;
    }
}
