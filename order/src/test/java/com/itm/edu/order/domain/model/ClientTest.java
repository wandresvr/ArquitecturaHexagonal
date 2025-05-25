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

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Client.builder()
                    .id(UUID.randomUUID())
                    .name("")
                    .email("john@example.com")
                    .phone("1234567890")
                    .build();
        });

        assertEquals("El nombre del cliente no puede estar vacío", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Client.builder()
                    .id(UUID.randomUUID())
                    .name("John Doe")
                    .email("")
                    .phone("1234567890")
                    .build();
        });

        assertEquals("El email del cliente no puede estar vacío", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsEmpty() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Client.builder()
                    .id(UUID.randomUUID())
                    .name("John Doe")
                    .email("john@example.com")
                    .phone("")
                    .build();
        });

        assertEquals("El teléfono del cliente no puede estar vacío", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testClientWithInvalidName(String invalidName) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Client.builder()
                    .name(invalidName)
                    .email("john.doe@example.com")
                    .phone("1234567890")
                    .build();
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testClientWithInvalidEmail(String invalidEmail) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Client.builder()
                    .name("John Doe")
                    .email(invalidEmail)
                    .phone("1234567890")
                    .build();
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testClientWithInvalidPhone(String invalidPhone) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Client.builder()
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .phone(invalidPhone)
                    .build();
        });
    }

    @Test
    void testClientBuilder() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        
        // Act
        Client client = Client.builder()
                .id(id)
                .name(name)
                .email(email)
                .phone(phone)
                .build();
        
        // Assert
        assertEquals(id, client.getId());
        assertEquals(name, client.getName());
        assertEquals(email, client.getEmail());
        assertEquals(phone, client.getPhone());
    }
} 