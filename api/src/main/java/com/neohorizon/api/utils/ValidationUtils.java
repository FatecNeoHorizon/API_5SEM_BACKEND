package com.neohorizon.api.utils;

import com.neohorizon.api.dto.response.fato.FatoApontamentoHorasDTO;
import com.neohorizon.api.dto.response.fato.FatoAtividadeDTO;
import com.neohorizon.api.exception.ValidationException;


public final class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária");
    }

    public static void validateFatoApontamentoHorasDTO(FatoApontamentoHorasDTO dto) {
        if (dto.getDimPeriodo() == null || dto.getDimPeriodo().getId() == null) {
           throw new IllegalArgumentException("Dimensão Período é obrigatória");
        }

        if (dto.getDimDev() == null || dto.getDimDev().getId() == null) {
           throw new IllegalArgumentException("Dimensão Desenvolvedor é obrigatória");
        }

        if (dto.getDimAtividade() == null || dto.getDimAtividade().getId() == null) {
           throw new IllegalArgumentException("Dimensão Atividade é obrigatória");
        }

        if (dto.getDimProjeto() == null || dto.getDimProjeto().getId() == null) {
           throw new IllegalArgumentException("Dimensão Projeto é obrigatória");
        }

        if (dto.getHorasTrabalhadas() == null || dto.getHorasTrabalhadas() <= 0) {
           throw new IllegalArgumentException("Horas trabalhadas deve ser maior que zero");
        }

        if (dto.getDescricaoTrabalho() == null || dto.getDescricaoTrabalho().isBlank()) {
           throw new IllegalArgumentException("Descrição do trabalho é obrigatória");
        }

        requireNonNull(dto.getHorasTrabalhadas(), "Horas trabalhadas");
        require(
            dto.getHorasTrabalhadas() > 0,
            "Horas trabalhadas deve ser maior que zero"
        );
    }

    public static void validateFatoAtividade(FatoAtividadeDTO fatoAtividadeDTO) {
        if (fatoAtividadeDTO.getDimProjeto() == null || fatoAtividadeDTO.getDimProjeto().getId() == null) {
            throw new IllegalArgumentException("DimProjeto id is required");
        }
        if (fatoAtividadeDTO.getDimPeriodo() == null || fatoAtividadeDTO.getDimPeriodo().getId() == null) {
            throw new IllegalArgumentException("DimPeriodo id is required");
        }
        if (fatoAtividadeDTO.getDimStatus() == null || fatoAtividadeDTO.getDimStatus().getId() == null) {
            throw new IllegalArgumentException("DimStatus id is required");
        }
        if (fatoAtividadeDTO.getDimTipo() == null || fatoAtividadeDTO.getDimTipo().getId() == null) {
            throw new IllegalArgumentException("DimTipo id is required");
        }
    }

    public static void requireNonNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new ValidationException(fieldName + " é obrigatório e não pode ser nulo");
        }
    }

    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " é obrigatório e não pode estar vazio");
        }
    }

    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new ValidationException(message);
        }
    }

    public static void requireValidId(Long id, String entityName) {
        requireNonNull(id, "ID do " + entityName + " é obrigatório");
        require(id > 0, "ID do " + entityName + " deve ser positivo");
    }

    public static void requireValidEmail(String email) {
        requireNonEmpty(email, "Email");
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Email deve ter um formato válido");
        }
    }
}