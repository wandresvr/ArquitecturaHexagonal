package com.itm.edu.order.infrastructure.exception;

import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.exception.HttpStatusException;
import com.itm.edu.order.domain.exception.OrderNotFoundException;
import com.itm.edu.order.domain.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleBusinessException() {
        // Arrange
        String errorMessage = "Error de negocio";
        BusinessException exception = new BusinessException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    void shouldHandleHttpStatusException() {
        // Arrange
        String errorMessage = "Error HTTP";
        HttpStatusException exception = HttpStatusException.badRequest(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpStatusException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getMessage());
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        
        FieldError fieldError = new FieldError("objectName", "field", "mensaje de error");
        when(bindingResult.getFieldErrors()).thenReturn(java.util.Collections.singletonList(fieldError));

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("field: mensaje de error"));
    }

    @Test
    void shouldHandleConstraintViolationException() {
        // Arrange
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("mensaje de error");
        when(violation.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
        when(violation.getPropertyPath().toString()).thenReturn("field");
        violations.add(violation);
        
        ConstraintViolationException exception = new ConstraintViolationException("Error de validación", violations);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleConstraintViolationException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("field: mensaje de error"));
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatchException() {
        // Arrange
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("paramName");
        when(exception.getValue()).thenReturn("invalidValue");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTypeMismatch(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("paramName"));
        assertTrue(response.getBody().getMessage().contains("invalidValue"));
    }

    @Test
    void handleProductNotFoundException() {
        // Arrange
        String errorMessage = "Producto no encontrado";
        ProductNotFoundException exception = new ProductNotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleProductNotFoundException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
    }

    @Test
    void handleOrderNotFoundException() {
        // Arrange
        String errorMessage = "Orden no encontrada";
        OrderNotFoundException exception = new OrderNotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleOrderNotFoundException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
    }

    @Test
    void handleGenericException() {
        // Arrange
        String errorMessage = "Error interno del servidor";
        Exception exception = new Exception(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }
} 