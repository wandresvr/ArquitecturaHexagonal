package com.itm.edu.order.infrastructure.exception;

import com.itm.edu.order.domain.exception.ApiError;
import com.itm.edu.order.domain.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        log.error("Error de negocio: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Error de validación: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError.of(HttpStatus.BAD_REQUEST, "Error de validación: " + ex.getMessage(), null));
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ApiError> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
        log.error("Error de acceso a datos: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError.of(HttpStatus.BAD_REQUEST, "Error al procesar la solicitud", null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));

        log.error("Error de validación: {}", message);
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, "Error de validación en los datos", null));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining("; "));

        log.error("Error de validación: {}", message);
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, "Error de validación en las restricciones", null));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("El parámetro '%s' con valor '%s' no puede ser convertido a tipo %s", 
            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
            
        log.error("Error de tipo de datos: {}", message);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError.of(HttpStatus.BAD_REQUEST, "Error en el tipo de datos de los parámetros", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: ", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error interno del servidor", null));
    }
} 