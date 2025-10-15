package com.neohorizon.api.utils;

import com.neohorizon.api.exception.ValidationException;

import java.util.function.Supplier;

public final class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária");
    }

    /**
     * Valida se um objeto não é nulo
     * @param object objeto a ser validado
     * @param message mensagem de erro
     * @throws ValidationException se o objeto for nulo
     */
    public static void requireNonNull(Object object, String message) {
        if (object == null) {
            throw new ValidationException(message);
        }
    }

    /**
     * Valida se uma string não é nula nem vazia
     * @param value string a ser validada
     * @param fieldName nome do campo para a mensagem de erro
     * @throws ValidationException se a string for nula ou vazia
     */
    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " é obrigatório e não pode estar vazio");
        }
    }

    /**
     * Valida se uma condição é verdadeira
     * @param condition condição a ser validada
     * @param message mensagem de erro se a condição for falsa
     * @throws ValidationException se a condição for falsa
     */
    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new ValidationException(message);
        }
    }

    /**
     * Valida se uma condição é verdadeira com supplier de exceção customizada
     * @param condition condição a ser validada
     * @param exceptionSupplier supplier da exceção a ser lançada
     * @throws RuntimeException exceção fornecida pelo supplier
     */
    public static void require(boolean condition, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (!condition) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Valida se um ID é positivo
     * @param id ID a ser validado
     * @param entityName nome da entidade para a mensagem de erro
     * @throws ValidationException se o ID for nulo ou não positivo
     */
    public static void requireValidId(Long id, String entityName) {
        requireNonNull(id, "ID do " + entityName + " é obrigatório");
        require(id > 0, "ID do " + entityName + " deve ser positivo");
    }

    /**
     * Valida se um email tem formato válido (validação básica)
     * @param email email a ser validado
     * @throws ValidationException se o email for inválido
     */
    public static void requireValidEmail(String email) {
        requireNonEmpty(email, "Email");
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Email deve ter um formato válido");
        }
    }
}