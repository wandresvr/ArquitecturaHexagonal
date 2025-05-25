package com.itm.edu.order.infrastructure.persistence;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.infrastructure.persistence.repository.JpaClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientRepositoryAdapterTest {

    @Mock
    private JpaClientRepository clientRepository;

    @InjectMocks
    private ClientRepositoryAdapter clientRepositoryAdapter;

    private Client testClient;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testClient = Client.builder()
                .id(testId)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();
    }

    @Test
    void shouldSaveClientSuccessfully() {
        // Arrange
        when(clientRepository.save(testClient)).thenReturn(testClient);

        // Act
        Client savedClient = clientRepositoryAdapter.save(testClient);

        // Assert
        assertNotNull(savedClient);
        assertEquals(testClient.getId(), savedClient.getId());
        assertEquals(testClient.getName(), savedClient.getName());
        assertEquals(testClient.getEmail(), savedClient.getEmail());
        assertEquals(testClient.getPhone(), savedClient.getPhone());
        verify(clientRepository).save(testClient);
    }

    @Test
    void shouldFindClientByIdSuccessfully() {
        // Arrange
        when(clientRepository.findById(testId)).thenReturn(Optional.of(testClient));

        // Act
        Optional<Client> foundClient = clientRepositoryAdapter.findById(testId);

        // Assert
        assertTrue(foundClient.isPresent());
        assertEquals(testClient.getId(), foundClient.get().getId());
        assertEquals(testClient.getName(), foundClient.get().getName());
        assertEquals(testClient.getEmail(), foundClient.get().getEmail());
        assertEquals(testClient.getPhone(), foundClient.get().getPhone());
        verify(clientRepository).findById(testId);
    }

    @Test
    void shouldReturnEmptyOptionalWhenClientNotFound() {
        // Arrange
        when(clientRepository.findById(testId)).thenReturn(Optional.empty());

        // Act
        Optional<Client> foundClient = clientRepositoryAdapter.findById(testId);

        // Assert
        assertFalse(foundClient.isPresent());
        verify(clientRepository).findById(testId);
    }
} 