package com.itm.edu.order.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("10.99");
        Integer stock = 100;

        // Act
        Product product = Product.create(name, description, price, stock);

        // Assert
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(stock, product.getStock());
    }

    @Test
    void shouldReconstituteProductSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("10.99");
        Integer stock = 100;

        // Act
        Product product = Product.reconstitute(id, name, description, price, stock);

        // Assert
        assertNotNull(product);
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(stock, product.getStock());
    }

    @Test
    void shouldUpdateProductDetailsSuccessfully() {
        // Arrange
        Product originalProduct = Product.create("Original", "Original Description", new BigDecimal("10.99"), 100);
        String newName = "Updated";
        String newDescription = "Updated Description";
        BigDecimal newPrice = new BigDecimal("20.99");
        Integer newStock = 200;

        // Act
        Product updatedProduct = originalProduct.withUpdatedDetails(newName, newDescription, newPrice, newStock);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals(originalProduct.getId(), updatedProduct.getId());
        assertEquals(newName, updatedProduct.getName());
        assertEquals(newDescription, updatedProduct.getDescription());
        assertEquals(newPrice, updatedProduct.getPrice());
        assertEquals(newStock, updatedProduct.getStock());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionWhenNameIsInvalid(String name) {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.create(name, "Test Description", new BigDecimal("10.99"), 100);
        });

        assertEquals("El nombre del producto no puede estar vacío", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionWhenDescriptionIsInvalid(String description) {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.create("Test Product", description, new BigDecimal("10.99"), 100);
        });

        assertEquals("La descripción del producto no puede estar vacía", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.create("Test Product", "Test Description", new BigDecimal("-10.99"), 100);
        });

        assertEquals("El precio no puede ser negativo", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.create("Test Product", "Test Description", new BigDecimal("10.99"), -100);
        });

        assertEquals("El stock no puede ser negativo", exception.getMessage());
    }

    @Test
    void testProductEquality() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("10.99");
        Integer stock = 100;

        // Act
        Product product1 = Product.reconstitute(id, name, description, price, stock);
        Product product2 = Product.reconstitute(id, name, description, price, stock);

        // Assert
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testProductInequality() {
        // Arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("10.99");
        Integer stock = 100;

        // Act
        Product product1 = Product.reconstitute(id1, name, description, price, stock);
        Product product2 = Product.reconstitute(id2, name, description, price, stock);

        // Assert
        assertNotEquals(product1, product2);
        assertNotEquals(product1.hashCode(), product2.hashCode());
    }
} 