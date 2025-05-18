package com.wilson.stock.domain.valueobjects;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void whenCreateQuantity_thenSuccess() {
        BigDecimal value = new BigDecimal("100.5");
        Quantity quantity = new Quantity();
        quantity.setValue(value);
        assertEquals(value, quantity.getValue());
    }

    @Test
    void whenCreateQuantityWithConstructor_thenSuccess() {
        BigDecimal value = new BigDecimal("100.5");
        Quantity quantity = new Quantity(value);
        assertEquals(value, quantity.getValue());
    }
} 