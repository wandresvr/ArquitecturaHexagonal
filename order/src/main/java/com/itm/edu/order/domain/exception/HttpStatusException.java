package com.itm.edu.order.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpStatusException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public HttpStatusException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    // 400 Bad Request
    public static HttpStatusException badRequest(String message) {
        return new HttpStatusException(HttpStatus.BAD_REQUEST, message);
    }

    // 401 Unauthorized
    public static HttpStatusException unauthorized(String message) {
        return new HttpStatusException(HttpStatus.UNAUTHORIZED, message);
    }

    // 403 Forbidden
    public static HttpStatusException forbidden(String message) {
        return new HttpStatusException(HttpStatus.FORBIDDEN, message);
    }

    // 404 Not Found
    public static HttpStatusException notFound(String message) {
        return new HttpStatusException(HttpStatus.NOT_FOUND, message);
    }

    // 409 Conflict
    public static HttpStatusException conflict(String message) {
        return new HttpStatusException(HttpStatus.CONFLICT, message);
    }

    // 422 Unprocessable Entity
    public static HttpStatusException unprocessableEntity(String message) {
        return new HttpStatusException(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    // 500 Internal Server Error
    public static HttpStatusException internalServerError(String message) {
        return new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
} 