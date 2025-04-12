package com.wilson.order.infrastructure.rest;

import com.wilson.order.application.ports.inputs.CreateOrderUseCase;
import com.wilson.order.domain.model.Order;
import com.wilson.order.domain.valueobjects.AddressShipping;
import com.wilson.order.infrastructure.rest.dto.AddressShippingDto;
import com.wilson.order.infrastructure.rest.dto.CreateOrderProductDto;
import com.wilson.order.infrastructure.rest.dto.CreateOrderRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
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
        UUID productId = UUID.randomUUID();
        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .customerName("John Doe")
                .products(Arrays.asList(
                        CreateOrderProductDto.builder()
                                .productId(productId)
                                .quantity(new BigDecimal("2"))
                                .build()
                ))
                .shippingAddress(AddressShippingDto.builder()
                        .street("123 Main St")
                        .city("New York")
                        .state("NY")
                        .zipCode("10001")
                        .country("USA")
                        .build())
                .build();

        Order expectedOrder = Order.builder()
                .orderId(UUID.randomUUID())
                .orderStatus("CREATED")
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
    }

    @Test
    void testCreateOrderWithEmptyProducts() {
        // Arrange
        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .customerName("John Doe")
                .products(Arrays.asList())
                .shippingAddress(AddressShippingDto.builder()
                        .street("123 Main St")
                        .city("New York")
                        .state("NY")
                        .zipCode("10001")
                        .country("USA")
                        .build())
                .build();

        Order expectedOrder = Order.builder()
                .orderId(UUID.randomUUID())
                .orderStatus("CREATED")
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
    }

    @Test
    void testCreateOrderWithMultipleProducts() {
        // Arrange
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .customerName("John Doe")
                .products(Arrays.asList(
                        CreateOrderProductDto.builder()
                                .productId(productId1)
                                .quantity(new BigDecimal("2"))
                                .build(),
                        CreateOrderProductDto.builder()
                                .productId(productId2)
                                .quantity(new BigDecimal("3"))
                                .build()
                ))
                .shippingAddress(AddressShippingDto.builder()
                        .street("123 Main St")
                        .city("New York")
                        .state("NY")
                        .zipCode("10001")
                        .country("USA")
                        .build())
                .build();

        Order expectedOrder = Order.builder()
                .orderId(UUID.randomUUID())
                .orderStatus("CREATED")
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
    }
} 