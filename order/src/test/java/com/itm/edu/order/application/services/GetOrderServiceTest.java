package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @InjectMocks
    private GetOrderService getOrderService;

    @Test
    void shouldGetOrderByIdSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        AddressShipping address = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        Order expectedOrder = Order.builder()
                .orderId(orderId)
                .client(client)
                .orderStatus("PENDING")
                .orderDate(LocalDateTime.now())
                .deliveryAddress(address)
                .build();

        when(orderRepositoryPort.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        // Act
        Optional<Order> result = getOrderService.getOrder(orderId);

        // Assert
        assertTrue(result.isPresent());
        Order order = result.get();
        assertEquals(orderId, order.getOrderId());
        assertEquals(client.getId(), order.getClient().getId());
        assertEquals(client.getName(), order.getClient().getName());
        assertEquals(client.getEmail(), order.getClient().getEmail());
        assertEquals(client.getPhone(), order.getClient().getPhone());
        assertEquals(address.getStreet(), order.getDeliveryAddress().getStreet());
        assertEquals(address.getCity(), order.getDeliveryAddress().getCity());
        assertEquals(address.getState(), order.getDeliveryAddress().getState());
        assertEquals(address.getZipCode(), order.getDeliveryAddress().getZipCode());
        assertEquals(address.getCountry(), order.getDeliveryAddress().getCountry());

        verify(orderRepositoryPort).findById(orderId);
        verifyNoMoreInteractions(orderRepositoryPort);
    }

    @Test
    void shouldReturnEmptyWhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepositoryPort.findById(orderId)).thenReturn(Optional.empty());

        // Act
        Optional<Order> result = getOrderService.getOrder(orderId);

        // Assert
        assertFalse(result.isPresent());
        verify(orderRepositoryPort).findById(orderId);
        verifyNoMoreInteractions(orderRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            getOrderService.getOrder(null);
        });

        assertEquals("Order ID cannot be null", exception.getMessage());
        verifyNoInteractions(orderRepositoryPort);
    }

    @Test
    void shouldGetAllOrdersSuccessfully() {
        // Arrange
        List<Order> expectedOrders = Arrays.asList(
                Order.builder()
                        .orderId(UUID.randomUUID())
                        .client(Client.builder()
                                .id(UUID.randomUUID())
                                .name("John Doe")
                                .email("john@example.com")
                                .phone("1234567890")
                                .build())
                        .orderStatus("PENDING")
                        .orderDate(LocalDateTime.now())
                        .deliveryAddress(AddressShipping.builder()
                                .street("123 Main St")
                                .city("New York")
                                .state("NY")
                                .zipCode("10001")
                                .country("USA")
                                .build())
                        .build(),
                Order.builder()
                        .orderId(UUID.randomUUID())
                        .client(Client.builder()
                                .id(UUID.randomUUID())
                                .name("Jane Doe")
                                .email("jane@example.com")
                                .phone("0987654321")
                                .build())
                        .orderStatus("COMPLETED")
                        .orderDate(LocalDateTime.now())
                        .deliveryAddress(AddressShipping.builder()
                                .street("456 Oak St")
                                .city("Los Angeles")
                                .state("CA")
                                .zipCode("90001")
                                .country("USA")
                                .build())
                        .build()
        );

        when(orderRepositoryPort.findAll()).thenReturn(expectedOrders);

        // Act
        List<Order> result = getOrderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedOrders.get(0).getOrderId(), result.get(0).getOrderId());
        assertEquals(expectedOrders.get(1).getOrderId(), result.get(1).getOrderId());
        verify(orderRepositoryPort).findAll();
        verifyNoMoreInteractions(orderRepositoryPort);
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersExist() {
        // Arrange
        when(orderRepositoryPort.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Order> result = getOrderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepositoryPort).findAll();
        verifyNoMoreInteractions(orderRepositoryPort);
    }
} 