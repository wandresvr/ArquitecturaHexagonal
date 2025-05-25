package com.itm.edu.order.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductValidationExceptionTest {

    @Test
    void shouldCreateProductValidationExceptionWithMessage() {
        // Arrange
        String errorMessage = "Error de validación del producto";

        // Act
        ProductValidationException exception = new ProductValidationException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldInheritFromRuntimeException() {
        // Arrange & Act
        ProductValidationException exception = new ProductValidationException("Error");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
} 