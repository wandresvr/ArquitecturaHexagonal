package com.itm.edu.order.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("99.99");
        Integer stock = 100;

        // Act
        Product product = Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();

        // Assert
        assertNotNull(product);
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(stock, product.getStock());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .id(UUID.randomUUID())
                    .name("")
                    .description("Test Description")
                    .price(new BigDecimal("99.99"))
                    .stock(100)
                    .build();
        });

        assertEquals("El nombre del producto no puede estar vacío", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .id(UUID.randomUUID())
                    .name("Test Product")
                    .description("")
                    .price(new BigDecimal("99.99"))
                    .stock(100)
                    .build();
        });

        assertEquals("La descripción del producto no puede estar vacía", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .id(UUID.randomUUID())
                    .name("Test Product")
                    .description("Test Description")
                    .price(new BigDecimal("-1.00"))
                    .stock(100)
                    .build();
        });

        assertEquals("El precio no puede ser negativo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .id(UUID.randomUUID())
                    .name("Test Product")
                    .description("Test Description")
                    .price(new BigDecimal("99.99"))
                    .stock(-1)
                    .build();
        });

        assertEquals("El stock no puede ser negativo", exception.getMessage());
    }

    @Test
    void shouldUpdateDetailsSuccessfully() {
        // Arrange
        Product originalProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Original Product")
                .description("Original Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        String newName = "Updated Product";
        String newDescription = "Updated Description";
        BigDecimal newPrice = new BigDecimal("149.99");
        Integer newStock = 200;

        // Act
        Product updatedProduct = originalProduct.withUpdatedDetails(
                newName,
                newDescription,
                newPrice,
                newStock
        );

        // Assert
        assertNotNull(updatedProduct);
        assertEquals(originalProduct.getId(), updatedProduct.getId());
        assertEquals(newName, updatedProduct.getName());
        assertEquals(newDescription, updatedProduct.getDescription());
        assertEquals(newPrice, updatedProduct.getPrice());
        assertEquals(newStock, updatedProduct.getStock());
    }
} 