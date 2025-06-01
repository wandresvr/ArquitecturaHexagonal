package com.itm.edu.order.infrastructure.persistence.repository;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.infrastructure.persistence.entities.ClientEntity;
import com.itm.edu.order.infrastructure.persistence.mapper.ClientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaClientRepositoryTest {

    @Mock
    private SpringJpaClientRepository jpaRepository;

    @Mock
    private ClientMapper clientMapper;

    private JpaClientRepository repository;

    private Client client;
    private ClientEntity clientEntity;
    private UUID clientId;

    @BeforeEach
    void setUp() {
        repository = new JpaClientRepository(jpaRepository, clientMapper);
        clientId = UUID.randomUUID();

        client = Client.builder()
            .id(clientId)
            .name("John Doe")
            .email("john@example.com")
            .phone("1234567890")
            .build();

        clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        clientEntity.setName("John Doe");
        clientEntity.setEmail("john@example.com");
        clientEntity.setPhone("1234567890");
    }

    @Test
    void shouldSaveClientSuccessfully() {
        // Arrange
        when(clientMapper.toEntity(client)).thenReturn(clientEntity);
        when(jpaRepository.save(clientEntity)).thenReturn(clientEntity);
        when(clientMapper.toDomain(clientEntity)).thenReturn(client);

        // Act
        Client savedClient = repository.save(client);

        // Assert
        assertNotNull(savedClient);
        assertEquals(client, savedClient);
        verify(clientMapper).toEntity(client);
        verify(jpaRepository).save(clientEntity);
        verify(clientMapper).toDomain(clientEntity);
    }

    @Test
    void shouldFindClientByIdWhenExists() {
        // Arrange
        when(jpaRepository.findById(clientId)).thenReturn(Optional.of(clientEntity));
        when(clientMapper.toDomain(clientEntity)).thenReturn(client);

        // Act
        Optional<Client> foundClient = repository.findById(clientId);

        // Assert
        assertTrue(foundClient.isPresent());
        assertEquals(client, foundClient.get());
        verify(jpaRepository).findById(clientId);
        verify(clientMapper).toDomain(clientEntity);
    }

    @Test
    void shouldReturnEmptyWhenClientNotFound() {
        // Arrange
        when(jpaRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act
        Optional<Client> foundClient = repository.findById(clientId);

        // Assert
        assertTrue(foundClient.isEmpty());
        verify(jpaRepository).findById(clientId);
        verify(clientMapper, never()).toDomain(any());
    }

    @Test
    void shouldFindAllClients() {
        // Arrange
        List<ClientEntity> entities = Arrays.asList(clientEntity);
        List<Client> clients = Arrays.asList(client);
        when(jpaRepository.findAll()).thenReturn(entities);
        when(clientMapper.toDomain(clientEntity)).thenReturn(client);

        // Act
        List<Client> foundClients = repository.findAll();

        // Assert
        assertNotNull(foundClients);
        assertEquals(clients, foundClients);
        verify(jpaRepository).findAll();
        verify(clientMapper).toDomain(clientEntity);
    }

    @Test
    void shouldDeleteClientById() {
        // Act
        repository.deleteById(clientId);

        // Assert
        verify(jpaRepository).deleteById(clientId);
    }
} 