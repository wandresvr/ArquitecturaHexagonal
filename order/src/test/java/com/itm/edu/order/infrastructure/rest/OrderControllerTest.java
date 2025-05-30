package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.*;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.infrastructure.rest.dto.*;
import com.itm.edu.order.infrastructure.rest.mapper.OrderDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    private UUID orderId;
    private Order order;
    private OrderDto orderDto;
    private CreateOrderRequest createOrderRequest;
    private CreateClientDto clientDto;
    private AddressShippingDto addressShippingDto;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        
        order = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING")
                .build();

        orderDto = OrderDto.builder()
                .orderId(orderId)
                .orderStatus("PENDING")
                .build();

        clientDto = new CreateClientDto();
        clientDto.setName("John Doe");
        clientDto.setEmail("john@example.com");
        clientDto.setPhone("1234567890");

        addressShippingDto = AddressShippingDto.builder()
                .street("123 Main St")
                .city("Test City")
                .state("Test State")
                .zipCode("12345")
                .country("Test Country")
                .build();

        AddressShipping addressShipping = AddressShipping.builder()
                .street(addressShippingDto.getStreet())
                .city(addressShippingDto.getCity())
                .state(addressShippingDto.getState())
                .zipCode(addressShippingDto.getZipCode())
                .country(addressShippingDto.getCountry())
                .build();

        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(UUID.randomUUID(), new BigDecimal("2"));

        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setClient(clientDto);
        createOrderRequest.setAddressShipping(addressShipping);
        createOrderRequest.setProductQuantities(productQuantities);
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(createOrderUseCase.createOrder(any(), any(), any())).thenReturn(order);
        when(orderDtoMapper.toDto(any(Order.class))).thenReturn(orderDto);

        // Act
        ResponseEntity<?> response = orderController.createOrder(createOrderRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(createOrderUseCase).createOrder(any(), any(), any());
        verify(orderDtoMapper).toDto(any(Order.class));
    }

    @Test
    void createOrder_NullClient() {
        // Arrange
        createOrderRequest.setClient(null);

        // Act
        ResponseEntity<?> response = orderController.createOrder(createOrderRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        verify(createOrderUseCase, never()).createOrder(any(), any(), any());
    }

    @Test
    void createOrder_NullAddress() {
        // Arrange
        createOrderRequest.setAddressShipping(null);

        // Act
        ResponseEntity<?> response = orderController.createOrder(createOrderRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        verify(createOrderUseCase, never()).createOrder(any(), any(), any());
    }

    @Test
    void getOrder_Success() {
        // Arrange
        when(getOrderUseCase.getOrder(orderId)).thenReturn(Optional.of(order));
        when(orderDtoMapper.toDto(order)).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> response = orderController.getOrder(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderDto, response.getBody());
    }

    @Test
    void getOrder_NotFound() {
        // Arrange
        when(getOrderUseCase.getOrder(orderId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<OrderDto> response = orderController.getOrder(orderId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllOrders_Success() {
        // Arrange
        List<Order> orders = Arrays.asList(order);
        List<OrderDto> orderDtos = Arrays.asList(orderDto);
        when(getOrderUseCase.getAllOrders()).thenReturn(orders);
        when(orderDtoMapper.toDto(any(Order.class))).thenReturn(orderDto);

        // Act
        ResponseEntity<List<OrderDto>> response = orderController.getAllOrders();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void updateOrder_Success() {
        // Arrange
        when(updateOrderUseCase.updateOrder(eq(orderId), any(Order.class))).thenReturn(order);
        when(orderDtoMapper.toDto(order)).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> response = orderController.updateOrder(orderId, orderDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderDto, response.getBody());
    }

    @Test
    void deleteOrder_Success() {
        // Arrange
        doNothing().when(deleteOrderUseCase).deleteOrder(orderId);

        // Act
        ResponseEntity<?> response = orderController.deleteOrder(orderId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteOrderUseCase).deleteOrder(orderId);
    }

    @Test
    void deleteOrder_BusinessException() {
        // Arrange
        doThrow(new BusinessException("Error al eliminar la orden")).when(deleteOrderUseCase).deleteOrder(orderId);

        // Act
        ResponseEntity<?> response = orderController.deleteOrder(orderId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateShippingAddress_Success() {
        // Arrange
        AddressShipping addressShipping = AddressShipping.builder()
                .street(addressShippingDto.getStreet())
                .city(addressShippingDto.getCity())
                .state(addressShippingDto.getState())
                .zipCode(addressShippingDto.getZipCode())
                .country(addressShippingDto.getCountry())
                .build();

        UpdateShippingAddressRequest request = new UpdateShippingAddressRequest();
        request.setAddressShipping(addressShipping);
        when(updateShippingAddressUseCase.updateShippingAddress(eq(orderId), any(AddressShipping.class))).thenReturn(order);
        when(orderDtoMapper.toDto(order)).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> response = orderController.updateShippingAddress(orderId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderDto, response.getBody());
    }

    @Test
    void updateShippingAddress_BusinessException() {
        // Arrange
        AddressShipping addressShipping = AddressShipping.builder()
                .street(addressShippingDto.getStreet())
                .city(addressShippingDto.getCity())
                .state(addressShippingDto.getState())
                .zipCode(addressShippingDto.getZipCode())
                .country(addressShippingDto.getCountry())
                .build();

        UpdateShippingAddressRequest request = new UpdateShippingAddressRequest();
        request.setAddressShipping(addressShipping);
        when(updateShippingAddressUseCase.updateShippingAddress(eq(orderId), any(AddressShipping.class)))
            .thenThrow(new BusinessException("Error al actualizar la dirección"));

        // Act & Assert
        assertThrows(BusinessException.class, () -> 
            orderController.updateShippingAddress(orderId, request));
    }
} 