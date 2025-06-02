package com.itm.edu.order.application.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

    @Test
    void shouldCreateProductDTOWithBuilder() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("10.99");
        Integer stock = 100;

        // Act
        ProductDTO productDTO = ProductDTO.builder()
            .id(id)
            .name(name)
            .description(description)
            .price(price)
            .stock(stock)
            .build();

        // Assert
        assertNotNull(productDTO);
        assertEquals(id, productDTO.getId());
        assertEquals(name, productDTO.getName());
        assertEquals(description, productDTO.getDescription());
        assertEquals(price, productDTO.getPrice());
        assertEquals(stock, productDTO.getStock());
    }

    @Test
    void shouldCreateProductDTOWithNoArgsConstructor() {
        // Act
        ProductDTO productDTO = new ProductDTO();

        // Assert
        assertNotNull(productDTO);
        assertNull(productDTO.getId());
        assertNull(productDTO.getName());
        assertNull(productDTO.getDescription());
        assertNull(productDTO.getPrice());
        assertNull(productDTO.getStock());
    }

    @Test
    void shouldCreateProductDTOWithAllArgsConstructor() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("10.99");
        Integer stock = 100;

        // Act
        ProductDTO productDTO = new ProductDTO(id, name, description, price, stock);

        // Assert
        assertNotNull(productDTO);
        assertEquals(id, productDTO.getId());
        assertEquals(name, productDTO.getName());
        assertEquals(description, productDTO.getDescription());
        assertEquals(price, productDTO.getPrice());
        assertEquals(stock, productDTO.getStock());
    }

    @Test
    void shouldSetAndGetValues() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("10.99");
        Integer stock = 100;

        // Act
        productDTO.setId(id);
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setPrice(price);
        productDTO.setStock(stock);

        // Assert
        assertEquals(id, productDTO.getId());
        assertEquals(name, productDTO.getName());
        assertEquals(description, productDTO.getDescription());
        assertEquals(price, productDTO.getPrice());
        assertEquals(stock, productDTO.getStock());
    }
} 