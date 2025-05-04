package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.exception.OrderNotFoundException;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateShippingAddressServiceTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @InjectMocks
    private UpdateShippingAddressService updateShippingAddressService;

    @Test
    void shouldUpdateShippingAddressSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        AddressShipping newAddress = AddressShipping.builder()
                .street("New Street")
                .city("New City")
                .state("New State")
                .zipCode("12345")
                .country("New Country")
                .build();

        Order existingOrder = Order.builder()
                .orderId(orderId)
                .deliveryAddress(AddressShipping.builder()
                        .street("Old Street")
                        .city("Old City")
                        .state("Old State")
                        .zipCode("54321")
                        .country("Old Country")
                        .build())
                .build();

        when(orderRepositoryPort.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepositoryPort.save(any(Order.class))).thenReturn(existingOrder);

        // Act
        Order updatedOrder = updateShippingAddressService.updateShippingAddress(orderId, newAddress);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(orderId, updatedOrder.getOrderId());
        assertEquals(newAddress.getStreet(), updatedOrder.getDeliveryAddress().getStreet());
        assertEquals(newAddress.getCity(), updatedOrder.getDeliveryAddress().getCity());
        assertEquals(newAddress.getState(), updatedOrder.getDeliveryAddress().getState());
        assertEquals(newAddress.getZipCode(), updatedOrder.getDeliveryAddress().getZipCode());
        assertEquals(newAddress.getCountry(), updatedOrder.getDeliveryAddress().getCountry());

        // Verify mock interactions
        verify(orderRepositoryPort, times(1)).findById(orderId);
        verify(orderRepositoryPort, times(1)).save(any(Order.class));
        verifyNoMoreInteractions(orderRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        AddressShipping newAddress = AddressShipping.builder()
                .street("New Street")
                .city("New City")
                .state("New State")
                .zipCode("12345")
                .country("New Country")
                .build();

        when(orderRepositoryPort.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            updateShippingAddressService.updateShippingAddress(orderId, newAddress);
        });

        assertEquals("Order not found", exception.getMessage());

        // Verify mock interactions
        verify(orderRepositoryPort, times(1)).findById(orderId);
        verifyNoMoreInteractions(orderRepositoryPort);
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenOrderIdIsNull(UUID nullId) {
        // Arrange
        AddressShipping newAddress = AddressShipping.builder()
                .street("New Street")
                .city("New City")
                .state("New State")
                .zipCode("12345")
                .country("New Country")
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateShippingAddressService.updateShippingAddress(nullId, newAddress);
        });

        assertEquals("Order ID cannot be null", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(orderRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        // Arrange
        UUID orderId = UUID.randomUUID();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateShippingAddressService.updateShippingAddress(orderId, null);
        });

        assertEquals("Shipping address cannot be null", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(orderRepositoryPort);
    }
} 