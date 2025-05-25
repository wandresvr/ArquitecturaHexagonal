package com.itm.edu.order.infrastructure.persistence.mapper;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.infrastructure.persistence.entities.ClientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapper();
    }

    @Test
    void shouldMapClientToDomainSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        ClientEntity entity = ClientEntity.builder()
                .id(id)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        // Act
        Client client = clientMapper.toDomain(entity);

        // Assert
        assertNotNull(client);
        assertEquals(id, client.getId());
        assertEquals("John Doe", client.getName());
        assertEquals("john@example.com", client.getEmail());
        assertEquals("1234567890", client.getPhone());
    }

    @Test
    void shouldMapClientToEntitySuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        Client client = Client.builder()
                .id(id)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        // Act
        ClientEntity entity = clientMapper.toEntity(client);

        // Assert
        assertNotNull(entity);
        assertEquals("John Doe", entity.getName());
        assertEquals("john@example.com", entity.getEmail());
        assertEquals("1234567890", entity.getPhone());
    }

    @Test
    void shouldReturnNullWhenMappingNullEntity() {
        // Act
        Client client = clientMapper.toDomain(null);

        // Assert
        assertNull(client);
    }

    @Test
    void shouldReturnNullWhenMappingNullClient() {
        // Act
        ClientEntity entity = clientMapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    void shouldHandleEmptyFieldsInEntity() {
        // Arrange
        UUID id = UUID.randomUUID();
        ClientEntity entity = ClientEntity.builder()
                .id(id)
                .name("")
                .email("")
                .phone("")
                .build();

        // Act
        Client client = clientMapper.toDomain(entity);

        // Assert
        assertNotNull(client);
        assertEquals(id, client.getId());
        assertEquals("Cliente sin nombre", client.getName());
        assertEquals("sin.email@dominio.com", client.getEmail());
        assertEquals("0000000000", client.getPhone());
    }

    @Test
    void shouldHandleNullFieldsInEntity() {
        // Arrange
        UUID id = UUID.randomUUID();
        ClientEntity entity = ClientEntity.builder()
                .id(id)
                .name(null)
                .email(null)
                .phone(null)
                .build();

        // Act
        Client client = clientMapper.toDomain(entity);

        // Assert
        assertNotNull(client);
        assertEquals(id, client.getId());
        assertEquals("Cliente sin nombre", client.getName());
        assertEquals("sin.email@dominio.com", client.getEmail());
        assertEquals("0000000000", client.getPhone());
    }

    @Test
    void shouldTrimFieldsWhenMappingToDomain() {
        // Arrange
        UUID id = UUID.randomUUID();
        ClientEntity entity = ClientEntity.builder()
                .id(id)
                .name("  John Doe  ")
                .email("  john@example.com  ")
                .phone("  1234567890  ")
                .build();

        // Act
        Client client = clientMapper.toDomain(entity);

        // Assert
        assertNotNull(client);
        assertEquals(id, client.getId());
        assertEquals("John Doe", client.getName().trim());
        assertEquals("john@example.com", client.getEmail().trim());
        assertEquals("1234567890", client.getPhone().trim());
    }
} 