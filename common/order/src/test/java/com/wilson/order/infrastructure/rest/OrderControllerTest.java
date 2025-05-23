package com.wilson.order.infrastructure.rest;

import com.wilson.order.application.ports.inputs.CreateOrderUseCase;
import com.wilson.order.domain.model.Client;
import com.wilson.order.domain.model.Order;
import com.wilson.order.domain.valueobjects.AddressShipping;
import com.wilson.order.infrastructure.rest.dto.AddressShippingDto;
import com.wilson.order.infrastructure.rest.dto.CreateClientDto;
import com.wilson.order.infrastructure.rest.dto.CreateOrderProductDto;
import com.wilson.order.infrastructure.rest.dto.CreateOrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderControllerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderSuccess() {
        // Arrange
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        CreateOrderProductDto productDto = CreateOrderProductDto.builder()
                .productId(UUID.randomUUID())
                .quantity(new BigDecimal("2"))
                .build();

        AddressShippingDto addressDto = AddressShippingDto.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .client(clientDto)
                .products(Arrays.asList(productDto))
                .shippingAddress(addressDto)
                .build();

        Client expectedClient = Client.builder()
                .name(clientDto.getName())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .build();

        AddressShipping expectedAddress = AddressShipping.builder()
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .zipCode(addressDto.getZipCode())
                .country(addressDto.getCountry())
                .build();

        Order expectedOrder = Order.builder()
                .orderId(UUID.randomUUID())
                .orderStatus("CREATED")
                .client(expectedClient)
                .deliveryAddress(expectedAddress)
                .build();

        when(createOrderUseCase.createOrder(any(), any(), any()))
                .thenReturn(expectedOrder);

        // Act
        ResponseEntity<Order> response = orderController.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(expectedOrder.getOrderId(), response.getBody().getOrderId());
        assertEquals(expectedOrder.getOrderStatus(), response.getBody().getOrderStatus());
        assertEquals(expectedClient.getName(), response.getBody().getClient().getName());
        assertEquals(expectedClient.getEmail(), response.getBody().getClient().getEmail());
        assertEquals(expectedClient.getPhone(), response.getBody().getClient().getPhone());
        assertEquals(expectedAddress.getStreet(), response.getBody().getDeliveryAddress().getStreet());
        assertEquals(expectedAddress.getCity(), response.getBody().getDeliveryAddress().getCity());
        assertEquals(expectedAddress.getState(), response.getBody().getDeliveryAddress().getState());
        assertEquals(expectedAddress.getZipCode(), response.getBody().getDeliveryAddress().getZipCode());
        assertEquals(expectedAddress.getCountry(), response.getBody().getDeliveryAddress().getCountry());
    }

    @Test
    void testCreateOrderWithMultipleProducts() {
        // Arrange
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        CreateOrderProductDto productDto1 = CreateOrderProductDto.builder()
                .productId(UUID.randomUUID())
                .quantity(new BigDecimal("2"))
                .build();

        CreateOrderProductDto productDto2 = CreateOrderProductDto.builder()
                .productId(UUID.randomUUID())
                .quantity(new BigDecimal("3"))
                .build();

        AddressShippingDto addressDto = AddressShippingDto.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .client(clientDto)
                .products(Arrays.asList(productDto1, productDto2))
                .shippingAddress(addressDto)
                .build();

        Client expectedClient = Client.builder()
                .name(clientDto.getName())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .build();

        AddressShipping expectedAddress = AddressShipping.builder()
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .zipCode(addressDto.getZipCode())
                .country(addressDto.getCountry())
                .build();

        Order expectedOrder = Order.builder()
                .orderId(UUID.randomUUID())
                .orderStatus("CREATED")
                .client(expectedClient)
                .deliveryAddress(expectedAddress)
                .build();

        when(createOrderUseCase.createOrder(any(), any(), any()))
                .thenReturn(expectedOrder);

        // Act
        ResponseEntity<Order> response = orderController.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(expectedOrder.getOrderId(), response.getBody().getOrderId());
        assertEquals(expectedOrder.getOrderStatus(), response.getBody().getOrderStatus());
        assertEquals(expectedClient.getName(), response.getBody().getClient().getName());
        assertEquals(expectedClient.getEmail(), response.getBody().getClient().getEmail());
        assertEquals(expectedClient.getPhone(), response.getBody().getClient().getPhone());
        assertEquals(expectedAddress.getStreet(), response.getBody().getDeliveryAddress().getStreet());
        assertEquals(expectedAddress.getCity(), response.getBody().getDeliveryAddress().getCity());
        assertEquals(expectedAddress.getState(), response.getBody().getDeliveryAddress().getState());
        assertEquals(expectedAddress.getZipCode(), response.getBody().getDeliveryAddress().getZipCode());
        assertEquals(expectedAddress.getCountry(), response.getBody().getDeliveryAddress().getCountry());
    }

    @Test
    void testCreateOrderWithInvalidData() {
        // Arrange
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        CreateOrderProductDto productDto = CreateOrderProductDto.builder()
                .productId(UUID.randomUUID())
                .quantity(new BigDecimal("0"))
                .build();

        AddressShippingDto addressDto = AddressShippingDto.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .client(clientDto)
                .products(Arrays.asList(productDto))
                .shippingAddress(addressDto)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderController.createOrder(request);
        });
    }
} 