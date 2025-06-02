package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.CreateOrderUseCase;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.infrastructure.rest.dto.CreateClientDto;
import com.itm.edu.order.infrastructure.rest.dto.CreateOrderRequest;
import com.itm.edu.order.infrastructure.rest.mapper.OrderDtoMapper;
import com.itm.edu.order.infrastructure.rest.dto.OrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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

    @Mock
    private OrderDtoMapper orderDtoMapper;

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

        OrderDto expectedOrderDto = OrderDto.builder()
                .orderStatus("PENDING_VALIDATION")
                .build();

        when(createOrderUseCase.createOrder(any(), any(), any())).thenReturn(expectedOrder);
        when(orderDtoMapper.toDto(expectedOrder)).thenReturn(expectedOrderDto);

        // Act
        ResponseEntity<?> response = orderController.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedOrderDto, response.getBody());
        verify(createOrderUseCase).createOrder(any(), any(), any());
        verify(orderDtoMapper).toDto(expectedOrder);
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
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        verifyNoInteractions(createOrderUseCase);
    }

    @Test
    void testCreateOrderWithNullClient() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(productId, new BigDecimal("1"));

        AddressShipping addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .client(null)
                .productQuantities(productQuantities)
                .addressShipping(addressShipping)
                .build();

        // Act
        ResponseEntity<?> response = orderController.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        verifyNoInteractions(createOrderUseCase);
    }

    @Test
    void testCreateOrderWithNullAddress() {
        // Arrange
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        UUID productId = UUID.randomUUID();
        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(productId, new BigDecimal("1"));

        CreateOrderRequest request = CreateOrderRequest.builder()
                .client(clientDto)
                .productQuantities(productQuantities)
                .addressShipping(null)
                .build();

        // Act
        ResponseEntity<?> response = orderController.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        verifyNoInteractions(createOrderUseCase);
    }

    @Test
    void testCreateOrderWithEmptyProductQuantities() {
        // Arrange
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        AddressShipping addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .client(clientDto)
                .productQuantities(new HashMap<>())
                .addressShipping(addressShipping)
                .build();

        // Act
        ResponseEntity<?> response = orderController.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        verifyNoInteractions(createOrderUseCase);
    }

    @Test
    void testCreateOrderWithNegativeQuantity() {
        // Arrange
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        UUID productId = UUID.randomUUID();
        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(productId, new BigDecimal("-1"));

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
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        verifyNoInteractions(createOrderUseCase);
    }
} 