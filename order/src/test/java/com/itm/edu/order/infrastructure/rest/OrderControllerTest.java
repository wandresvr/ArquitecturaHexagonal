package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.CreateOrderUseCase;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.infrastructure.rest.dto.CreateClientDto;
import com.itm.edu.order.infrastructure.rest.dto.CreateOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        UUID productId = UUID.randomUUID();
        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(productId, new BigDecimal("2"));

        AddressShipping addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .client(clientDto)
                .productQuantities(productQuantities)
                .addressShipping(addressShipping)
                .build();

        Client expectedClient = Client.builder()
                .name(clientDto.getName())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .build();

        Order expectedOrder = Order.builder()
                .orderStatus("PENDING_VALIDATION")
                .client(expectedClient)
                .deliveryAddress(addressShipping)
                .build();

        when(createOrderUseCase.createOrder(any(), any(), any())).thenReturn(expectedOrder);

        // Act
        ResponseEntity<?> response = orderController.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        verify(createOrderUseCase).createOrder(any(), any(), any());
    }

    @Test
    void testCreateOrderWithInvalidData() {
        // Arrange
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        UUID productId = UUID.randomUUID();
        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(productId, BigDecimal.ZERO);

        AddressShipping addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .client(clientDto)
                .productQuantities(productQuantities)
                .addressShipping(addressShipping)
                .build();

        // Act
        ResponseEntity<?> response = orderController.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(422, response.getStatusCode().value());
    }
} 