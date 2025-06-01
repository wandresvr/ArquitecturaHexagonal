package com.itm.edu.order.domain.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTest {

    @Test
    void shouldCreateApiErrorWithBasicInfo() {
        // Arrange
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "El producto no fue encontrado";
        String path = "/api/products/123";

        // Act
        ApiError apiError = ApiError.of(status, message, path);

        // Assert
        assertNotNull(apiError);
        assertNotNull(apiError.getTimestamp());
        assertTrue(apiError.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertEquals(status, apiError.getStatus());
        assertEquals(message, apiError.getMessage());
        assertEquals(path, apiError.getPath());
        assertNull(apiError.getErrors());
    }

    @Test
    void shouldCreateApiErrorWithErrorsList() {
        // Arrange
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Error de validación";
        String path = "/api/products";
        List<String> errors = Arrays.asList(
            "El nombre del producto es requerido",
            "El precio debe ser mayor que cero"
        );

        // Act
        ApiError apiError = ApiError.of(status, message, path, errors);

        // Assert
        assertNotNull(apiError);
        assertNotNull(apiError.getTimestamp());
        assertTrue(apiError.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertEquals(status, apiError.getStatus());
        assertEquals(message, apiError.getMessage());
        assertEquals(path, apiError.getPath());
        assertNotNull(apiError.getErrors());
        assertEquals(2, apiError.getErrors().size());
        assertEquals(errors.get(0), apiError.getErrors().get(0));
        assertEquals(errors.get(1), apiError.getErrors().get(1));
    }

    @Test
    void shouldCreateApiErrorWithBuilder() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Error interno del servidor";
        String path = "/api/orders";
        List<String> errors = Arrays.asList("Error al procesar la solicitud");

        // Act
        ApiError apiError = ApiError.builder()
            .timestamp(timestamp)
            .status(status)
            .message(message)
            .path(path)
            .errors(errors)
            .build();

        // Assert
        assertNotNull(apiError);
        assertEquals(timestamp, apiError.getTimestamp());
        assertEquals(status, apiError.getStatus());
        assertEquals(message, apiError.getMessage());
        assertEquals(path, apiError.getPath());
        assertNotNull(apiError.getErrors());
        assertEquals(1, apiError.getErrors().size());
        assertEquals(errors.get(0), apiError.getErrors().get(0));
    }
} 