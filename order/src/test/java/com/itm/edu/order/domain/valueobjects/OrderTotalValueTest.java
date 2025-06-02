package com.itm.edu.order.domain.valueobjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTotalValueTest {

    @Test
    void shouldCreateOrderTotalValueSuccessfully() {
        // Arrange
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "USD";

        // Act
        OrderTotalValue totalValue = new OrderTotalValue(amount, currency);

        // Assert
        assertNotNull(totalValue);
        assertEquals(amount, totalValue.getAmount());
        assertEquals(currency, totalValue.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new OrderTotalValue(new BigDecimal("-100.00"), "USD");
        });

        assertEquals("El monto total no puede ser negativo", exception.getMessage());
    }

    @Test
    void shouldAddAmountSuccessfully() {
        // Arrange
        OrderTotalValue totalValue = new OrderTotalValue(new BigDecimal("100.00"), "USD");
        BigDecimal amountToAdd = new BigDecimal("50.00");

        // Act
        totalValue.addAmount(amountToAdd);

        // Assert
        assertEquals(new BigDecimal("150.00"), totalValue.getAmount());
    }

    @Test
    void shouldThrowExceptionWhenAddingNegativeAmount() {
        // Arrange
        OrderTotalValue totalValue = new OrderTotalValue(new BigDecimal("100.00"), "USD");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            totalValue.addAmount(new BigDecimal("-50.00"));
        });

        assertEquals("El monto a agregar no puede ser negativo", exception.getMessage());
    }

    @Test
    void shouldSubtractAmountSuccessfully() {
        // Arrange
        OrderTotalValue totalValue = new OrderTotalValue(new BigDecimal("100.00"), "USD");
        BigDecimal amountToSubtract = new BigDecimal("50.00");

        // Act
        totalValue.subtractAmount(amountToSubtract);

        // Assert
        assertEquals(new BigDecimal("50.00"), totalValue.getAmount());
    }

    @Test
    void shouldThrowExceptionWhenSubtractingNegativeAmount() {
        // Arrange
        OrderTotalValue totalValue = new OrderTotalValue(new BigDecimal("100.00"), "USD");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            totalValue.subtractAmount(new BigDecimal("-50.00"));
        });

        assertEquals("El monto a restar no puede ser negativo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenResultIsNegative() {
        // Arrange
        OrderTotalValue totalValue = new OrderTotalValue(new BigDecimal("100.00"), "USD");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            totalValue.subtractAmount(new BigDecimal("150.00"));
        });

        assertEquals("El monto resultante no puede ser negativo", exception.getMessage());
    }

    @Test
    void shouldCreateZeroValueSuccessfully() {
        // Act
        OrderTotalValue zeroValue = OrderTotalValue.zero();

        // Assert
        assertNotNull(zeroValue);
        assertEquals(BigDecimal.ZERO, zeroValue.getAmount());
        assertEquals("USD", zeroValue.getCurrency());
    }

    @Test
    void testOrderTotalValueEquality() {
        // Arrange
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "USD";

        // Act
        OrderTotalValue value1 = new OrderTotalValue(amount, currency);
        OrderTotalValue value2 = new OrderTotalValue(amount, currency);

        // Assert
        assertEquals(value1, value2);
        assertEquals(value1.hashCode(), value2.hashCode());
    }

    @Test
    void testOrderTotalValueInequality() {
        // Arrange
        OrderTotalValue value1 = new OrderTotalValue(new BigDecimal("100.00"), "USD");
        OrderTotalValue value2 = new OrderTotalValue(new BigDecimal("200.00"), "USD");

        // Assert
        assertNotEquals(value1, value2);
        assertNotEquals(value1.hashCode(), value2.hashCode());
    }

    @Test
    void testOrderTotalValueInequalityWithDifferentCurrency() {
        // Arrange
        OrderTotalValue value1 = new OrderTotalValue(new BigDecimal("100.00"), "USD");
        OrderTotalValue value2 = new OrderTotalValue(new BigDecimal("100.00"), "EUR");

        // Assert
        assertNotEquals(value1, value2);
        assertNotEquals(value1.hashCode(), value2.hashCode());
    }
} 