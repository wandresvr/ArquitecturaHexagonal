package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.*;
import com.itm.edu.order.domain.model.*;
import com.itm.edu.order.domain.valueobjects.*;
import com.itm.edu.order.infrastructure.rest.dto.*;
import com.itm.edu.order.infrastructure.rest.mapper.OrderDtoMapper;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.exception.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private GetOrderUseCase getOrderUseCase;

    @Mock
    private DeleteOrderUseCase deleteOrderUseCase;

    @Mock
    private UpdateOrderUseCase updateOrderUseCase;

    @Mock
    private UpdateShippingAddressUseCase updateShippingAddressUseCase;

    @Mock
    private OrderDtoMapper orderDtoMapper;

    @InjectMocks
    private OrderController orderController;

    private CreateOrderRequest createOrderRequest;
    private Order order;
    private OrderDto orderDto;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Configurar CreateOrderRequest
        CreateClientDto clientDto = CreateClientDto.builder()
            .name("John Doe")
            .email("john@example.com")
            .phone("1234567890")
            .build();

        AddressShipping addressShipping = AddressShipping.builder()
            .street("123 Main St")
            .city("Medellín")
            .state("Antioquia")
            .zipCode("050001")
            .country("Colombia")
            .build();

        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(productId, new BigDecimal("2"));

        createOrderRequest = CreateOrderRequest.builder()
            .client(clientDto)
            .addressShipping(addressShipping)
            .productQuantities(productQuantities)
            .build();

        // Configurar Order
        Client client = Client.builder()
            .id(UUID.randomUUID())
            .name("John Doe")
            .email("john@example.com")
            .phone("1234567890")
            .build();

        order = Order.builder()
            .orderId(orderId)
            .client(client)
            .deliveryAddress(addressShipping)
            .orderStatus("PENDING")
            .orderDate(LocalDateTime.now())
            .build();

        // Configurar OrderDto
        orderDto = OrderDto.builder()
            .orderId(orderId)
            .orderStatus("PENDING")
            .build();
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        when(createOrderUseCase.createOrder(
            any(CreateClientDto.class),
            anyMap(),
            any(AddressShipping.class)
        )).thenReturn(order);
        when(orderDtoMapper.toDto(order)).thenReturn(orderDto);

        // Act
        ResponseEntity<?> response = orderController.createOrder(createOrderRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderDto, response.getBody());
        verify(createOrderUseCase).createOrder(
            any(CreateClientDto.class),
            anyMap(),
            any(AddressShipping.class)
        );
        verify(orderDtoMapper).toDto(order);
    }

    @Test
    void shouldReturnUnprocessableEntityWhenClientIsNull() {
        // Arrange
        createOrderRequest.setClient(null);

        // Act
        ResponseEntity<?> response = orderController.createOrder(createOrderRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiError);
        ApiError apiError = (ApiError) response.getBody();
        assertEquals("El cliente no puede ser nulo", apiError.getMessage());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenAddressIsNull() {
        // Arrange
        createOrderRequest.setAddressShipping(null);

        // Act
        ResponseEntity<?> response = orderController.createOrder(createOrderRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiError);
        ApiError apiError = (ApiError) response.getBody();
        assertEquals("La dirección de envío no puede ser nula", apiError.getMessage());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenProductQuantitiesIsEmpty() {
        // Arrange
        createOrderRequest.setProductQuantities(new HashMap<>());

        // Act
        ResponseEntity<?> response = orderController.createOrder(createOrderRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiError);
        ApiError apiError = (ApiError) response.getBody();
        assertEquals("Debe especificar al menos un producto", apiError.getMessage());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenQuantityIsZero() {
        // Arrange
        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(UUID.randomUUID(), BigDecimal.ZERO);
        createOrderRequest.setProductQuantities(productQuantities);

        // Act
        ResponseEntity<?> response = orderController.createOrder(createOrderRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiError);
        ApiError apiError = (ApiError) response.getBody();
        assertEquals("La cantidad debe ser mayor a cero", apiError.getMessage());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenBusinessExceptionOccurs() {
        // Arrange
        when(createOrderUseCase.createOrder(
            any(CreateClientDto.class),
            anyMap(),
            any(AddressShipping.class)
        )).thenThrow(new BusinessException("Error de negocio"));

        // Act
        ResponseEntity<?> response = orderController.createOrder(createOrderRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiError);
        ApiError apiError = (ApiError) response.getBody();
        assertEquals("Error de negocio", apiError.getMessage());
    }

    @Test
    void shouldGetOrderSuccessfully() {
        // Arrange
        when(getOrderUseCase.getOrder(orderId)).thenReturn(Optional.of(order));
        when(orderDtoMapper.toDto(order)).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> response = orderController.getOrder(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDto, response.getBody());
        verify(getOrderUseCase).getOrder(orderId);
        verify(orderDtoMapper).toDto(order);
    }

    @Test
    void shouldReturnNotFoundWhenOrderDoesNotExist() {
        // Arrange
        when(getOrderUseCase.getOrder(orderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<OrderDto> response = orderController.getOrder(orderId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(getOrderUseCase).getOrder(orderId);
        verify(orderDtoMapper, never()).toDto(any());
    }

    @Test
    void shouldGetAllOrdersSuccessfully() {
        // Arrange
        List<Order> orders = Arrays.asList(order);
        List<OrderDto> orderDtos = Arrays.asList(orderDto);
        when(getOrderUseCase.getAllOrders()).thenReturn(orders);
        when(orderDtoMapper.toDto(order)).thenReturn(orderDto);

        // Act
        ResponseEntity<List<OrderDto>> response = orderController.getAllOrders();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDtos, response.getBody());
        verify(getOrderUseCase).getAllOrders();
        verify(orderDtoMapper).toDto(order);
    }

    @Test
    void shouldUpdateOrderSuccessfully() {
        // Arrange
        when(updateOrderUseCase.updateOrder(eq(orderId), any(Order.class))).thenReturn(order);
        when(orderDtoMapper.toDto(order)).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> response = orderController.updateOrder(orderId, orderDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDto, response.getBody());
        verify(updateOrderUseCase).updateOrder(eq(orderId), any(Order.class));
        verify(orderDtoMapper).toDto(order);
    }

    @Test
    void shouldDeleteOrderSuccessfully() {
        // Act
        ResponseEntity<?> response = orderController.deleteOrder(orderId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteOrderUseCase).deleteOrder(orderId);
    }

    @Test
    void shouldReturnBadRequestWhenDeleteOrderThrowsBusinessException() {
        // Arrange
        doThrow(new BusinessException("Error al eliminar la orden"))
            .when(deleteOrderUseCase).deleteOrder(orderId);

        // Act
        ResponseEntity<?> response = orderController.deleteOrder(orderId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiError);
        ApiError apiError = (ApiError) response.getBody();
        assertEquals("Error al eliminar la orden", apiError.getMessage());
    }

    @Test
    void shouldUpdateShippingAddressSuccessfully() {
        // Arrange
        AddressShipping addressShipping = AddressShipping.builder()
            .street("456 New St")
            .city("Bogotá")
            .state("Cundinamarca")
            .zipCode("110111")
            .country("Colombia")
            .build();

        UpdateShippingAddressRequest request = UpdateShippingAddressRequest.builder()
            .addressShipping(addressShipping)
            .build();

        when(updateShippingAddressUseCase.updateShippingAddress(eq(orderId), any(AddressShipping.class)))
            .thenReturn(order);
        when(orderDtoMapper.toDto(order)).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> response = orderController.updateShippingAddress(orderId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDto, response.getBody());
        verify(updateShippingAddressUseCase).updateShippingAddress(eq(orderId), any(AddressShipping.class));
        verify(orderDtoMapper).toDto(order);
    }
} 