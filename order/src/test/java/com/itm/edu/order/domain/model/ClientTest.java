package com.itm.edu.order.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void shouldCreateClientSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        String email = "john@example.com";
        String phone = "1234567890";

        // Act
        Client client = Client.builder()
                .id(id)
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        // Assert
        assertNotNull(client);
        assertEquals(id, client.getId());
        assertEquals(name, client.getName());
        assertEquals(email, client.getEmail());
        assertEquals(phone, client.getPhone());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionWhenNameIsInvalid(String name) {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Client.builder()
                    .id(UUID.randomUUID())
                    .name(name)
                    .email("john@example.com")
                    .phone("1234567890")
                    .build();
        });

        assertEquals("El nombre del cliente no puede estar vacío", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionWhenEmailIsInvalid(String email) {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Client.builder()
                    .id(UUID.randomUUID())
                    .name("John Doe")
                    .email(email)
                    .phone("1234567890")
                    .build();
        });

        assertEquals("El email del cliente no puede estar vacío", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionWhenPhoneIsInvalid(String phone) {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Client.builder()
                    .id(UUID.randomUUID())
                    .name("John Doe")
                    .email("john@example.com")
                    .phone(phone)
                    .build();
        });

        assertEquals("El teléfono del cliente no puede estar vacío", exception.getMessage());
    }

    @Test
    void shouldCreateClientWithoutId() {
        // Arrange
        String name = "John Doe";
        String email = "john@example.com";
        String phone = "1234567890";

        // Act
        Client client = Client.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        // Assert
        assertNotNull(client);
        assertNull(client.getId());
        assertEquals(name, client.getName());
        assertEquals(email, client.getEmail());
        assertEquals(phone, client.getPhone());
    }
} 