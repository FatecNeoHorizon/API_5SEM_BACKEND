package com.neohorizon.api.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.neohorizon.api.exception.ValidationException;

public class ValidationUtilsTest {

    @Test
    void deveTerConstrutorPrivado() throws Exception {
        Constructor<ValidationUtils> validador = ValidationUtils.class.getDeclaredConstructor();
        Assertions.assertTrue(validador.canAccess(null) == false, "Construtor deveria ser privado");

        validador.setAccessible(true);

        InvocationTargetException exception = Assertions.assertThrows(
            InvocationTargetException.class,
            validador::newInstance
        );

        Throwable causa = exception.getCause();

        Assertions.assertInstanceOf(UnsupportedOperationException.class, causa);
        Assertions.assertEquals("Esta é uma classe utilitária", causa.getMessage());
    }
    
    @Test
    void verificaValidacaoEmail() {
        Assertions.assertThrows(ValidationException.class, () -> ValidationUtils.requireValidEmail(""));
        Assertions.assertThrows(ValidationException.class, () -> ValidationUtils.requireValidEmail("testeemailsempadrao@"));
        Assertions.assertDoesNotThrow(() -> ValidationUtils.requireValidEmail("teste@dominio"));
    }
    
    @Test
    void verificaValidacaoID() {
        Assertions.assertThrows(ValidationException.class, () -> ValidationUtils.requireValidId(0L, "teste"));
        Assertions.assertThrows(ValidationException.class, () -> ValidationUtils.requireValidId(-10L, "teste"));
        Assertions.assertDoesNotThrow(() -> ValidationUtils.requireValidId(10L, "teste"));
    }
    
    @Test
    void verificaValidacaoNaoVazio() {
        Assertions.assertThrows(ValidationException.class, () -> ValidationUtils.requireNonEmpty("", ""));
        Assertions.assertThrows(ValidationException.class, () -> ValidationUtils.requireNonEmpty(null, ""));
        Assertions.assertDoesNotThrow(() -> ValidationUtils.requireNonEmpty("teste", ""));
    }
    
    @Test
    void verificaValidacaoNaoNulo() {
        Assertions.assertThrows(ValidationException.class, () -> ValidationUtils.requireNonNull(null, ""));
        Assertions.assertDoesNotThrow(() -> ValidationUtils.requireNonNull("teste", ""));
    }

    @Test
    void requireComMensagemDeveLancarExcecao() {
        Assertions.assertThrows(ValidationException.class,
                () -> ValidationUtils.require(false, "Falha na validação"));

        Assertions.assertDoesNotThrow(
                () -> ValidationUtils.require(true, "Falha na validação")
        );
    }

    @Test
    void requireComSupplierDeveLancarExcecao() {
        Assertions.assertThrows(ValidationException.class,
                () -> ValidationUtils.require(false, "ID válido"));

        Assertions.assertDoesNotThrow(
                () -> ValidationUtils.require(true, "ID inválido")
        );
    }

}
