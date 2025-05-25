package com.itm.edu.order.infrastructure.rest.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class OrderTotalDtoTest {

    @Test
    void shouldCreateOrderTotalDtoSuccessfully() {
        // Arrange
        BigDecimal amount = new BigDecimal("199.99");
        String currency = "USD";

        // Act
        OrderTotalDto totalDto = OrderTotalDto.builder()
                .amount(amount)
                .currency(currency)
                .build();

        // Assert
        assertNotNull(totalDto);
        assertEquals(amount, totalDto.getAmount());
        assertEquals(currency, totalDto.getCurrency());
    }

    @Test
    void shouldCreateEmptyOrderTotalDto() {
        // Act
        OrderTotalDto totalDto = OrderTotalDto.builder().build();

        // Assert
        assertNotNull(totalDto);
        assertNull(totalDto.getAmount());
        assertNull(totalDto.getCurrency());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        // Arrange
        OrderTotalDto totalDto = new OrderTotalDto();
        BigDecimal amount = new BigDecimal("199.99");
        String currency = "USD";

        // Act
        totalDto.setAmount(amount);
        totalDto.setCurrency(currency);

        // Assert
        assertEquals(amount, totalDto.getAmount());
        assertEquals(currency, totalDto.getCurrency());
    }
} 