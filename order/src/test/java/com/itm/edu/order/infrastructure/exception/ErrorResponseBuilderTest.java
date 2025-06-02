package com.itm.edu.order.infrastructure.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseBuilderTest {

    @Test
    void errorResponseBuilder_ShouldSetAllFieldsCorrectly() {
        // Arrange
        int status = 400;
        String message = "Mensaje de error";
        String timestamp = "2024-06-01T12:00:00";

        // Act
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .message(message)
                .timestamp(timestamp)
                .build();

        // Assert
        assertNotNull(errorResponse);
        assertEquals(status, errorResponse.getStatus());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(timestamp, errorResponse.getTimestamp());
    }

    @Test
    void errorResponseBuilder_ShouldHandleNullValues() {
        // Arrange
        int status = 500;
        String message = null;
        String timestamp = null;

        // Act
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .message(message)
                .timestamp(timestamp)
                .build();

        // Assert
        assertNotNull(errorResponse);
        assertEquals(status, errorResponse.getStatus());
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getTimestamp());
    }

    @Test
    void errorResponseBuilder_ShouldCreateInstanceWithDefaultValues() {
        // Act
        ErrorResponse errorResponse = ErrorResponse.builder().build();

        // Assert
        assertNotNull(errorResponse);
        assertEquals(0, errorResponse.getStatus());
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getTimestamp());
    }

    @Test
    void errorResponseBuilder_ShouldCreateDifferentInstances() {
        // Arrange
        ErrorResponse errorResponse1 = ErrorResponse.builder()
                .status(400)
                .message("Error 1")
                .timestamp("2024-06-01T12:00:00")
                .build();

        ErrorResponse errorResponse2 = ErrorResponse.builder()
                .status(500)
                .message("Error 2")
                .timestamp("2024-06-01T13:00:00")
                .build();

        // Assert
        assertNotEquals(errorResponse1.getStatus(), errorResponse2.getStatus());
        assertNotEquals(errorResponse1.getMessage(), errorResponse2.getMessage());
        assertNotEquals(errorResponse1.getTimestamp(), errorResponse2.getTimestamp());
    }

    @Test
    void errorResponseBuilder_ShouldCreateInstanceWithPartialValues() {
        // Arrange
        int status = 404;
        String message = "Not Found";

        // Act
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .message(message)
                .build();

        // Assert
        assertNotNull(errorResponse);
        assertEquals(status, errorResponse.getStatus());
        assertEquals(message, errorResponse.getMessage());
        assertNull(errorResponse.getTimestamp());
    }
} 