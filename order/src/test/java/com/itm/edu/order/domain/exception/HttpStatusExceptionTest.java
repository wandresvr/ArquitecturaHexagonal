package com.itm.edu.order.domain.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class HttpStatusExceptionTest {

    @Test
    void shouldCreateBadRequestException() {
        // Arrange
        String message = "Error de solicitud inválida";

        // Act
        HttpStatusException exception = HttpStatusException.badRequest(message);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateUnauthorizedException() {
        // Arrange
        String message = "No autorizado";

        // Act
        HttpStatusException exception = HttpStatusException.unauthorized(message);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateForbiddenException() {
        // Arrange
        String message = "Acceso prohibido";

        // Act
        HttpStatusException exception = HttpStatusException.forbidden(message);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateNotFoundException() {
        // Arrange
        String message = "Recurso no encontrado";

        // Act
        HttpStatusException exception = HttpStatusException.notFound(message);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateConflictException() {
        // Arrange
        String message = "Conflicto de recursos";

        // Act
        HttpStatusException exception = HttpStatusException.conflict(message);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateUnprocessableEntityException() {
        // Arrange
        String message = "Entidad no procesable";

        // Act
        HttpStatusException exception = HttpStatusException.unprocessableEntity(message);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateInternalServerErrorException() {
        // Arrange
        String message = "Error interno del servidor";

        // Act
        HttpStatusException exception = HttpStatusException.internalServerError(message);

        // Assert
        assertNotNull(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateCustomHttpStatusException() {
        // Arrange
        String message = "Error personalizado";
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;

        // Act
        HttpStatusException exception = new HttpStatusException(status, message);

        // Assert
        assertNotNull(exception);
        assertEquals(status, exception.getStatus());
        assertEquals(message, exception.getMessage());
    }
} 