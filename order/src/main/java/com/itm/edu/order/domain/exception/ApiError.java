package com.itm.edu.order.domain.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "Modelo de respuesta de error de la API")
public class ApiError {
    @Schema(description = "Fecha y hora del error", example = "2024-03-14T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "NOT_FOUND")
    private HttpStatus status;

    @Schema(description = "Mensaje de error", example = "El producto no fue encontrado")
    private String message;

    @Schema(description = "Ruta de la solicitud", example = "/api/products/123")
    private String path;

    @Schema(description = "Lista de errores detallados", example = "[\"El ID del producto es inválido\"]")
    private List<String> errors;

    public static ApiError of(HttpStatus status, String message, String path) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .path(path)
                .build();
    }

    public static ApiError of(HttpStatus status, String message, String path, List<String> errors) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .path(path)
                .errors(errors)
                .build();
    }
} 