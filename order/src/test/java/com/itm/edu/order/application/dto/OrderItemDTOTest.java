package com.itm.edu.order.application.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemDTOTest {

    @Test
    void shouldCreateOrderItemDTOWithBuilder() {
        // Arrange
        UUID id = UUID.randomUUID();
        ProductDTO product = ProductDTO.builder()
            .id(UUID.randomUUID())
            .name("Test Product")
            .description("Test Description")
            .price(new BigDecimal("10.99"))
            .stock(100)
            .build();
        BigDecimal quantity = new BigDecimal("2");
        BigDecimal value = new BigDecimal("21.98");

        // Act
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
            .id(id)
            .product(product)
            .quantity(quantity)
            .value(value)
            .build();

        // Assert
        assertNotNull(orderItemDTO);
        assertEquals(id, orderItemDTO.getId());
        assertEquals(product, orderItemDTO.getProduct());
        assertEquals(quantity, orderItemDTO.getQuantity());
        assertEquals(value, orderItemDTO.getValue());
    }

    @Test
    void shouldCreateOrderItemDTOWithNoArgsConstructor() {
        // Act
        OrderItemDTO orderItemDTO = new OrderItemDTO();

        // Assert
        assertNotNull(orderItemDTO);
        assertNull(orderItemDTO.getId());
        assertNull(orderItemDTO.getProduct());
        assertNull(orderItemDTO.getQuantity());
        assertNull(orderItemDTO.getValue());
    }

    @Test
    void shouldCreateOrderItemDTOWithAllArgsConstructor() {
        // Arrange
        UUID id = UUID.randomUUID();
        ProductDTO product = ProductDTO.builder()
            .id(UUID.randomUUID())
            .name("Test Product")
            .description("Test Description")
            .price(new BigDecimal("10.99"))
            .stock(100)
            .build();
        BigDecimal quantity = new BigDecimal("2");
        BigDecimal value = new BigDecimal("21.98");

        // Act
        OrderItemDTO orderItemDTO = new OrderItemDTO(id, product, quantity, value);

        // Assert
        assertNotNull(orderItemDTO);
        assertEquals(id, orderItemDTO.getId());
        assertEquals(product, orderItemDTO.getProduct());
        assertEquals(quantity, orderItemDTO.getQuantity());
        assertEquals(value, orderItemDTO.getValue());
    }

    @Test
    void shouldSetAndGetValues() {
        // Arrange
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        UUID id = UUID.randomUUID();
        ProductDTO product = ProductDTO.builder()
            .id(UUID.randomUUID())
            .name("Test Product")
            .description("Test Description")
            .price(new BigDecimal("10.99"))
            .stock(100)
            .build();
        BigDecimal quantity = new BigDecimal("2");
        BigDecimal value = new BigDecimal("21.98");

        // Act
        orderItemDTO.setId(id);
        orderItemDTO.setProduct(product);
        orderItemDTO.setQuantity(quantity);
        orderItemDTO.setValue(value);

        // Assert
        assertEquals(id, orderItemDTO.getId());
        assertEquals(product, orderItemDTO.getProduct());
        assertEquals(quantity, orderItemDTO.getQuantity());
        assertEquals(value, orderItemDTO.getValue());
    }
} 