package com.itm.edu.order.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void shouldCreateBusinessExceptionWithMessage() {
        // Arrange
        String errorMessage = "Error de negocio";

        // Act
        BusinessException exception = new BusinessException(errorMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldInheritFromRuntimeException() {
        // Arrange & Act
        BusinessException exception = new BusinessException("Error");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
} 