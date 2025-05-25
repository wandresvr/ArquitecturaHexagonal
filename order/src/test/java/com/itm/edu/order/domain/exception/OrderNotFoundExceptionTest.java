package com.itm.edu.order.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderNotFoundExceptionTest {

    @Test
    void shouldCreateOrderNotFoundExceptionWithMessage() {
        // Arrange
        String errorMessage = "Orden no encontrada";

        // Act
        OrderNotFoundException exception = new OrderNotFoundException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldInheritFromRuntimeException() {
        // Arrange & Act
        OrderNotFoundException exception = new OrderNotFoundException("Error");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
} 