package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @InjectMocks
    private UpdateOrderService updateOrderService;

    @Test
    void shouldUpdateOrderStatusSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order existingOrder = createSampleOrder(orderId);
        Order orderWithNewStatus = existingOrder.withUpdatedStatus("COMPLETED");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(orderWithNewStatus);

        // Act
        Order updatedOrder = updateOrderService.updateOrder(orderId, Order.builder()
                .orderStatus("COMPLETED")
                .build());

        // Assert
        assertNotNull(updatedOrder);
        assertEquals("COMPLETED", updatedOrder.getOrderStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldUpdateClientSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order existingOrder = createSampleOrder(orderId);
        
        Client newClient = Client.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .email("jane@example.com")
                .phone("9876543210")
                .build();
        
        Order orderWithNewClient = existingOrder.withUpdatedClient(newClient);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(orderWithNewClient);

        // Act
        Order updatedOrder = updateOrderService.updateOrder(orderId, Order.builder()
                .client(newClient)
                .build());

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(newClient.getName(), updatedOrder.getClient().getName());
        assertEquals(newClient.getEmail(), updatedOrder.getClient().getEmail());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldUpdateDeliveryAddressSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order existingOrder = createSampleOrder(orderId);
        
        AddressShipping newAddress = AddressShipping.builder()
                .street("456 Second St")
                .city("Los Angeles")
                .state("CA")
                .zipCode("90001")
                .country("USA")
                .build();
        
        Order orderWithNewAddress = existingOrder.withUpdatedDeliveryAddress(newAddress);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(orderWithNewAddress);

        // Act
        Order updatedOrder = updateOrderService.updateOrder(orderId, Order.builder()
                .deliveryAddress(newAddress)
                .build());

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(newAddress.getStreet(), updatedOrder.getDeliveryAddress().getStreet());
        assertEquals(newAddress.getCity(), updatedOrder.getDeliveryAddress().getCity());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
            updateOrderService.updateOrder(orderId, Order.builder().orderStatus("COMPLETED").build())
        );

        assertEquals("Orden no encontrada con ID: " + orderId, exception.getMessage());
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    private Order createSampleOrder(UUID orderId) {
        return Order.builder()
                .orderId(orderId)
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
                .products(new ArrayList<>())
                .build();
    }
} 