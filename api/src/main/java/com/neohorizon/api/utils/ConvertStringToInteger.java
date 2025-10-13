package com.neohorizon.api.utils;

public class ConvertStringToInteger {

    private ConvertStringToInteger() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converte uma string de data no formato "AAAA-MM-DD" para um array de Integers [Ano, Mês, Dia].
     *
     * @param value A string de data a ser convertida.
     * @return Um array de Integers contendo [Ano, Mês, Dia].
     * @throws IllegalArgumentException Se a string de data for nula, vazia, não estiver no formato "AAAA-MM-DD",
     * ou se os componentes da data não forem números válidos.
     */
    public static Integer[] convertStringDateToInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("A string de data não pode ser nula ou vazia.");
        }

        String[] parts = value.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Formato de data inválido. Esperado 'AAAA-MM-DD'. Recebido: '" + value + "'");
        }

        try {
            return new Integer[] {
                Integer.valueOf(parts[0]),
                Integer.valueOf(parts[1]),
                Integer.valueOf(parts[2])
            };
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Um ou mais componentes da data não são números válidos. Esperado: AAAA-MM-DD. Recebido: '" + value + "'", e);
        }
    }
}