package com.itm.edu.order.infrastructure.exception;

import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.exception.HttpStatusException;
import com.itm.edu.order.domain.exception.OrderNotFoundException;
import com.itm.edu.order.domain.exception.ProductNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleBusinessException_ShouldReturnBadRequest() {
        // Arrange
        String errorMessage = "Error de negocio";
        BusinessException ex = new BusinessException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleHttpStatusException_ShouldReturnSpecifiedStatus() {
        // Arrange
        String errorMessage = "Error HTTP";
        HttpStatus status = HttpStatus.FORBIDDEN;
        HttpStatusException ex = new HttpStatusException(status, errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpStatusException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(status, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(status.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleValidationExceptions_ShouldReturnBadRequest() {
        // Arrange
        FieldError fieldError = new FieldError("object", "field", "error message");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("field: error message"));
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleConstraintViolationException_ShouldReturnBadRequest() {
        // Arrange
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolationException ex = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertNotNull(response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleTypeMismatch_ShouldReturnBadRequest() {
        // Arrange
        String paramName = "id";
        String paramValue = "abc";
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn(paramName);
        when(ex.getValue()).thenReturn(paramValue);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleTypeMismatch(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains(paramName));
        assertTrue(response.getBody().getMessage().contains(paramValue));
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleProductNotFoundException_ShouldReturnNotFound() {
        // Arrange
        String errorMessage = "Producto no encontrado";
        ProductNotFoundException ex = new ProductNotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleProductNotFoundException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleOrderNotFoundException_ShouldReturnNotFound() {
        // Arrange
        String errorMessage = "Orden no encontrada";
        OrderNotFoundException ex = new OrderNotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleOrderNotFoundException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        // Arrange
        Exception ex = new RuntimeException("Error interno");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(ex);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }
} 