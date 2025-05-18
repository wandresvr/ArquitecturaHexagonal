package com.itm.edu.stock.domain.valueobjects;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void whenCreateQuantity_thenSuccess() {
        BigDecimal value = new BigDecimal("100.5");
        Quantity quantity = new Quantity(value);
        assertEquals(0, value.compareTo(quantity.getValue()), "Los valores deberían ser iguales");
    }

    @Test
    void whenCompareQuantities_thenSuccess() {
        Quantity smaller = new Quantity(new BigDecimal("10.0"));
        Quantity larger = new Quantity(new BigDecimal("20.0"));
        assertTrue(smaller.isLessThan(larger.getValue()), "10.0 debería ser menor que 20.0");
        assertFalse(larger.isLessThan(smaller.getValue()), "20.0 no debería ser menor que 10.0");
    }

    @Test
    void whenSubtractQuantities_thenSuccess() {
        Quantity initial = new Quantity(new BigDecimal("30.0"));
        BigDecimal subtrahend = new BigDecimal("10.0");
        Quantity result = initial.subtract(subtrahend);
        assertEquals(0, new BigDecimal("20.0").compareTo(result.getValue()), "30.0 - 10.0 debería ser 20.0");
    }
} 