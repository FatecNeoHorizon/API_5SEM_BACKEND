package com.neohorizon.api.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConvertStringToIntegerTest {

    @Test
    void deveTerConstrutorPrivado() throws Exception {
        Constructor<ConvertStringToInteger> converter = ConvertStringToInteger.class.getDeclaredConstructor();
        Assertions.assertTrue(converter.canAccess(null) == false, "Construtor deveria ser privado");

        converter.setAccessible(true);

        InvocationTargetException exception = Assertions.assertThrows(
            InvocationTargetException.class,
            converter::newInstance
        );

        Throwable causa = exception.getCause();

        Assertions.assertEquals("Utility class", causa.getMessage());
    }
    
    @Test
    void deveLancarExcecaoNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ConvertStringToInteger.convertStringDateToInteger(""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ConvertStringToInteger.convertStringDateToInteger(null));
    }

    @Test
    void deveLancarExcecaoFormatoInvalido() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ConvertStringToInteger.convertStringDateToInteger("2025-03-02-04"));
    }
    
    @Test
    void deveLancarExcecaoNaoInteiro() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ConvertStringToInteger.convertStringDateToInteger("2025-03-s"));
    }

    @Test
    void realizaConversaoStringInteiro() {
        Integer[] resultado = ConvertStringToInteger.convertStringDateToInteger("2025-03-01");
        Integer[] esperado = new Integer[]{2025, 3, 1};
        Assertions.assertArrayEquals(esperado, resultado);
    }
}

