package com.wilson.order.infrastructure.rest.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidProductRequest() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Test Product");
        request.setDescription("Test Description");
        request.setPrice(new BigDecimal("99.99"));
        request.setStock(100);

        // Act
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenNameIsEmpty() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("");
        request.setDescription("Test Description");
        request.setPrice(new BigDecimal("99.99"));
        request.setStock(100);

        // Act
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Name is required"));
    }

    @Test
    void shouldFailWhenPriceIsNegative() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Test Product");
        request.setDescription("Test Description");
        request.setPrice(new BigDecimal("-99.99"));
        request.setStock(100);

        // Act
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Price must be greater than or equal to 0"));
    }

    @Test
    void shouldFailWhenStockIsNegative() {
        // Arrange
        ProductRequest request = new ProductRequest();
        request.setName("Test Product");
        request.setDescription("Test Description");
        request.setPrice(new BigDecimal("99.99"));
        request.setStock(-100);

        // Act
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Stock must be greater than or equal to 0"));
    }
} 