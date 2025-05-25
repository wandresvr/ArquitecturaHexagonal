package com.itm.edu.stock.infrastructure.api.exception;

import com.itm.edu.stock.domain.exception.BusinessException;
import com.itm.edu.stock.infrastructure.api.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleBusinessException() {
        // Arrange
        String errorMessage = "Error de negocio";
        BusinessException exception = new BusinessException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals(errorMessage, response.getBody().getMessage());
    }

    @Test
    void handleValidationExceptions() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        FieldError fieldError = new FieldError("object", "field", "mensaje de error");
        when(bindingResult.getAllErrors()).thenReturn(java.util.Collections.singletonList(fieldError));

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("field"));
        assertTrue(response.getBody().getMessage().contains("mensaje de error"));
    }

    @Test
    void handleGlobalException() {
        // Arrange
        String errorMessage = "Error interno";
        Exception exception = new Exception(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGlobalException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains(errorMessage));
    }
} 