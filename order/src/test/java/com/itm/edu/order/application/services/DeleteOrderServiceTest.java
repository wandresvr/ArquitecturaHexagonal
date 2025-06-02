package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.exception.OrderNotFoundException;
import com.itm.edu.order.domain.model.Order;
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
class DeleteOrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @InjectMocks
    private DeleteOrderService deleteOrderService;

    @Test
    void shouldDeleteOrderSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order existingOrder = Order.builder()
                .orderId(orderId)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        doNothing().when(orderRepository).deleteById(orderId);

        // Act
        assertDoesNotThrow(() -> deleteOrderService.deleteOrder(orderId));

        // Assert
        verify(orderRepository).findById(orderId);
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> deleteOrderService.deleteOrder(null));
        assertEquals("Order ID cannot be null", exception.getMessage());
        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        UUID nonExistentOrderId = UUID.randomUUID();
        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        // Act & Assert
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> deleteOrderService.deleteOrder(nonExistentOrderId));
        assertEquals("Order not found with id: " + nonExistentOrderId, exception.getMessage());
        verify(orderRepository).findById(nonExistentOrderId);
        verify(orderRepository, never()).deleteById(any());
    }
} 