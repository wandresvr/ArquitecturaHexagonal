package com.itm.edu.order.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    @Test
    void shouldCreateProductNotFoundExceptionWithMessage() {
        // Arrange
        String errorMessage = "Producto no encontrado";

        // Act
        ProductNotFoundException exception = new ProductNotFoundException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldInheritFromRuntimeException() {
        // Arrange & Act
        ProductNotFoundException exception = new ProductNotFoundException("Error");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
} 