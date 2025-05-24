package com.itm.edu.stock.domain.valueobjects;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void testQuantityCreation() {
        // Arrange
        BigDecimal value = new BigDecimal("100.5");

        // Act
        Quantity quantity = new Quantity(value);

        // Assert
        assertEquals(0, value.compareTo(quantity.getValue()), "Los valores deberían ser iguales");
    }

    @Test
    void testQuantityComparison() {
        // Arrange
        Quantity smaller = new Quantity(new BigDecimal("10.0"));
        Quantity larger = new Quantity(new BigDecimal("20.0"));

        // Assert
        assertTrue(smaller.isLessThan(larger), "10.0 debería ser menor que 20.0");
        assertFalse(larger.isLessThan(smaller), "20.0 no debería ser menor que 10.0");
    }

    @Test
    void testQuantityArithmetic() {
        // Arrange
        Quantity initial = new Quantity(new BigDecimal("30.0"));
        Quantity subtrahend = new Quantity(new BigDecimal("10.0"));

        // Act
        Quantity result = initial.subtract(subtrahend);

        // Assert
        assertEquals(0, new BigDecimal("20.0").compareTo(result.getValue()), "30.0 - 10.0 debería ser 20.0");
    }

    @Test
    void testQuantityValidation() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Quantity(null));
        assertThrows(IllegalArgumentException.class, () -> new Quantity(new BigDecimal("-1")));
    }

    @Test
    void testQuantityEquality() {
        // Arrange
        Quantity quantity1 = new Quantity(new BigDecimal("10.0"));
        Quantity quantity2 = new Quantity(new BigDecimal("10.0"));
        Quantity quantity3 = new Quantity(new BigDecimal("20.0"));

        // Assert
        assertEquals(quantity1, quantity2);
        assertNotEquals(quantity1, quantity3);
        assertEquals(quantity1.hashCode(), quantity2.hashCode());
    }
} 