package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateShippingAddressServiceTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @InjectMocks
    private UpdateShippingAddressService updateShippingAddressService;

    @Test
    void shouldUpdateShippingAddressSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        AddressShipping newAddress = AddressShipping.builder()
                .street("Nueva Calle 123")
                .city("Nueva Ciudad")
                .state("Nuevo Estado")
                .zipCode("54321")
                .country("Nuevo País")
                .build();

        Order existingOrder = Order.builder()
                .orderId(orderId)
                .deliveryAddress(AddressShipping.builder()
                        .street("Vieja Calle 123")
                        .city("Vieja Ciudad")
                        .state("Viejo Estado")
                        .zipCode("12345")
                        .country("Viejo País")
                        .build())
                .build();

        Order updatedOrder = Order.builder()
                .orderId(orderId)
                .deliveryAddress(newAddress)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // Act
        Order result = updateShippingAddressService.updateShippingAddress(orderId, newAddress);

        // Assert
        assertNotNull(result);
        assertEquals(newAddress, result.getDeliveryAddress());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        // Arrange
        AddressShipping newAddress = AddressShipping.builder()
                .street("Nueva Calle 123")
                .city("Nueva Ciudad")
                .state("Nuevo Estado")
                .zipCode("54321")
                .country("Nuevo País")
                .build();

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                updateShippingAddressService.updateShippingAddress(null, newAddress));

        assertEquals("El ID de la orden no puede ser nulo", exception.getMessage());
        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        // Arrange
        UUID orderId = UUID.randomUUID();

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                updateShippingAddressService.updateShippingAddress(orderId, null));

        assertEquals("La dirección de envío no puede ser nula", exception.getMessage());
        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        AddressShipping newAddress = AddressShipping.builder()
                .street("Nueva Calle 123")
                .city("Nueva Ciudad")
                .state("Nuevo Estado")
                .zipCode("54321")
                .country("Nuevo País")
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                updateShippingAddressService.updateShippingAddress(orderId, newAddress));

        assertEquals("Orden no encontrada: " + orderId, exception.getMessage());
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenAddressHasEmptyStreet() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AddressShipping.builder()
                    .street("")
                    .city("New City")
                    .state("New State")
                    .zipCode("12345")
                    .country("New Country")
                    .build();
        });

        assertEquals("La calle no puede estar vacía", exception.getMessage());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenAddressHasEmptyCity() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AddressShipping.builder()
                    .street("New Street")
                    .city("")
                    .state("New State")
                    .zipCode("12345")
                    .country("New Country")
                    .build();
        });

        assertEquals("La ciudad no puede estar vacía", exception.getMessage());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenAddressHasEmptyState() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AddressShipping.builder()
                    .street("New Street")
                    .city("New City")
                    .state("")
                    .zipCode("12345")
                    .country("New Country")
                    .build();
        });

        assertEquals("El estado no puede estar vacío", exception.getMessage());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenAddressHasEmptyZipCode() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AddressShipping.builder()
                    .street("New Street")
                    .city("New City")
                    .state("New State")
                    .zipCode("")
                    .country("New Country")
                    .build();
        });

        assertEquals("El código postal no puede estar vacío", exception.getMessage());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldThrowExceptionWhenAddressHasEmptyCountry() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            AddressShipping.builder()
                    .street("New Street")
                    .city("New City")
                    .state("New State")
                    .zipCode("12345")
                    .country("")
                    .build();
        });

        assertEquals("El país no puede estar vacío", exception.getMessage());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldPreserveOtherOrderPropertiesWhenUpdatingAddress() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String orderStatus = "PENDING";
        AddressShipping newAddress = AddressShipping.builder()
                .street("New Street")
                .city("New City")
                .state("New State")
                .zipCode("12345")
                .country("New Country")
                .build();

        Order existingOrder = Order.builder()
                .orderId(orderId)
                .orderStatus(orderStatus)
                .deliveryAddress(AddressShipping.builder()
                        .street("Old Street")
                        .city("Old City")
                        .state("Old State")
                        .zipCode("54321")
                        .country("Old Country")
                        .build())
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = updateShippingAddressService.updateShippingAddress(orderId, newAddress);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(orderId, updatedOrder.getOrderId());
        assertEquals(orderStatus, updatedOrder.getOrderStatus());
        assertEquals(newAddress.getStreet(), updatedOrder.getDeliveryAddress().getStreet());
        assertEquals(newAddress.getCity(), updatedOrder.getDeliveryAddress().getCity());
        assertEquals(newAddress.getState(), updatedOrder.getDeliveryAddress().getState());
        assertEquals(newAddress.getZipCode(), updatedOrder.getDeliveryAddress().getZipCode());
        assertEquals(newAddress.getCountry(), updatedOrder.getDeliveryAddress().getCountry());

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
        verifyNoMoreInteractions(orderRepository);
    }

    private Order createTestOrder(UUID orderId) {
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        AddressShipping address = AddressShipping.builder()
                .street("Old Street")
                .city("Old City")
                .state("Old State")
                .zipCode("54321")
                .country("Old Country")
                .build();

        return Order.builder()
                .orderId(orderId)
                .client(client)
                .orderStatus("PENDING")
                .orderDate(LocalDateTime.now())
                .deliveryAddress(address)
                .products(new ArrayList<>())
                .total(new OrderTotalValue(BigDecimal.ZERO, "USD"))
                .build();
    }
} 