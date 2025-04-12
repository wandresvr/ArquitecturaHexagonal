package com.wilson.order.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testClientGettersAndSetters() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        String email = "john.doe@example.com";
        String phone = "1234567890";
        
        Client client = new Client();
        
        // Act
        client.setId(id);
        client.setName(name);
        client.setEmail(email);
        client.setPhone(phone);
        
        // Assert
        assertEquals(id, client.getId());
        assertEquals(name, client.getName());
        assertEquals(email, client.getEmail());
        assertEquals(phone, client.getPhone());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testClientWithInvalidName(String invalidName) {
        // Arrange & Act & Assert
        Client client = Client.builder()
                .name(invalidName)
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
        
        assertThrows(IllegalArgumentException.class, client::validate);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testClientWithInvalidEmail(String invalidEmail) {
        // Arrange & Act & Assert
        Client client = Client.builder()
                .name("John Doe")
                .email(invalidEmail)
                .phone("1234567890")
                .build();
        
        assertThrows(IllegalArgumentException.class, client::validate);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testClientWithInvalidPhone(String invalidPhone) {
        // Arrange & Act & Assert
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone(invalidPhone)
                .build();
        
        assertThrows(IllegalArgumentException.class, client::validate);
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