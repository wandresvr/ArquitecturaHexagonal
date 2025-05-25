package com.itm.edu.order.infrastructure.rest.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class CreateOrderProductDtoTest {

    @Test
    void shouldCreateCreateOrderProductDtoSuccessfully() {
        // Arrange
        UUID productId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("2");

        // Act
        CreateOrderProductDto productDto = CreateOrderProductDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        // Assert
        assertNotNull(productDto);
        assertEquals(productId, productDto.getProductId());
        assertEquals(quantity, productDto.getQuantity());
    }

    @Test
    void shouldCreateEmptyCreateOrderProductDto() {
        // Act
        CreateOrderProductDto productDto = CreateOrderProductDto.builder().build();

        // Assert
        assertNotNull(productDto);
        assertNull(productDto.getProductId());
        assertNull(productDto.getQuantity());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        // Arrange
        CreateOrderProductDto productDto = new CreateOrderProductDto();
        UUID productId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("2");

        // Act
        productDto.setProductId(productId);
        productDto.setQuantity(quantity);

        // Assert
        assertEquals(productId, productDto.getProductId());
        assertEquals(quantity, productDto.getQuantity());
    }
} 