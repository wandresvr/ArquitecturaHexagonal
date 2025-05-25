package com.itm.edu.order.infrastructure.rest.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CreateClientDtoTest {

    @Test
    void shouldCreateCreateClientDtoSuccessfully() {
        // Arrange
        String name = "John Doe";
        String email = "john@example.com";
        String phone = "1234567890";

        // Act
        CreateClientDto clientDto = CreateClientDto.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        // Assert
        assertNotNull(clientDto);
        assertEquals(name, clientDto.getName());
        assertEquals(email, clientDto.getEmail());
        assertEquals(phone, clientDto.getPhone());
    }

    @Test
    void shouldCreateEmptyCreateClientDto() {
        // Act
        CreateClientDto clientDto = CreateClientDto.builder().build();

        // Assert
        assertNotNull(clientDto);
        assertNull(clientDto.getName());
        assertNull(clientDto.getEmail());
        assertNull(clientDto.getPhone());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        // Arrange
        CreateClientDto clientDto = new CreateClientDto();
        String name = "John Doe";
        String email = "john@example.com";
        String phone = "1234567890";

        // Act
        clientDto.setName(name);
        clientDto.setEmail(email);
        clientDto.setPhone(phone);

        // Assert
        assertEquals(name, clientDto.getName());
        assertEquals(email, clientDto.getEmail());
        assertEquals(phone, clientDto.getPhone());
    }
} 