package com.itm.edu.order.infrastructure.rest.mapper;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import com.itm.edu.order.infrastructure.rest.dto.OrderDto;
import com.itm.edu.order.infrastructure.rest.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderDtoMapperTest {

    @Mock
    private ProductDtoMapper productDtoMapper;

    private OrderDtoMapper orderDtoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderDtoMapper = new OrderDtoMapper(productDtoMapper);
    }

    @Test
    void shouldMapOrderToDtoSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        LocalDateTime orderDate = LocalDateTime.now();
        String orderStatus = "PENDING";

        Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();

        AddressShipping deliveryAddress = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        OrderTotalValue total = new OrderTotalValue(new BigDecimal("199.98"), "USD");

        Order order = Order.builder()
                .orderId(orderId)
                .client(client)
                .products(Arrays.asList(orderItem))
                .deliveryAddress(deliveryAddress)
                .total(total)
                .orderDate(orderDate)
                .orderStatus(orderStatus)
                .build();

        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();

        when(productDtoMapper.toDto(product)).thenReturn(productDto);

        // Act
        OrderDto dto = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(dto);
        assertEquals(orderId, dto.getOrderId());
        assertEquals(orderStatus, dto.getOrderStatus());
        assertEquals(orderDate, dto.getOrderDate());

        // Verify client mapping
        assertNotNull(dto.getClient());
        assertEquals(client.getId(), dto.getClient().getId());
        assertEquals(client.getName(), dto.getClient().getName());
        assertEquals(client.getEmail(), dto.getClient().getEmail());
        assertEquals(client.getPhone(), dto.getClient().getPhone());

        // Verify order items mapping
        assertNotNull(dto.getItems());
        assertEquals(1, dto.getItems().size());
        OrderDto.OrderItemDto mappedItem = dto.getItems().get(0);
        assertEquals(productDto.getId(), mappedItem.getProduct().getId());
        assertEquals(productDto.getName(), mappedItem.getProduct().getName());
        assertEquals(productDto.getDescription(), mappedItem.getProduct().getDescription());
        assertEquals(productDto.getPrice(), mappedItem.getProduct().getPrice());
        assertEquals(productDto.getStock(), mappedItem.getProduct().getStock());
        assertEquals(orderItem.getQuantity(), mappedItem.getQuantity());

        // Verify address mapping
        assertNotNull(dto.getShippingAddress());
        assertEquals(deliveryAddress.getStreet(), dto.getShippingAddress().getStreet());
        assertEquals(deliveryAddress.getCity(), dto.getShippingAddress().getCity());
        assertEquals(deliveryAddress.getState(), dto.getShippingAddress().getState());
        assertEquals(deliveryAddress.getZipCode(), dto.getShippingAddress().getZipCode());
        assertEquals(deliveryAddress.getCountry(), dto.getShippingAddress().getCountry());

        // Verify total mapping
        assertNotNull(dto.getTotal());
        assertEquals(total.getAmount(), dto.getTotal().getAmount());
        assertEquals(total.getCurrency(), dto.getTotal().getCurrency());

        verify(productDtoMapper).toDto(product);
    }

    @Test
    void shouldReturnNullWhenOrderIsNull() {
        // Act
        OrderDto dto = orderDtoMapper.toDto(null);

        // Assert
        assertNull(dto);
        verifyNoInteractions(productDtoMapper);
    }

    @Test
    void shouldHandleNullClientInOrder() {
        // Arrange
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .client(null)
                .products(Arrays.asList())
                .deliveryAddress(null)
                .total(null)
                .orderDate(LocalDateTime.now())
                .orderStatus("PENDING")
                .build();

        // Act
        OrderDto dto = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(dto);
        assertNull(dto.getClient());
        verifyNoInteractions(productDtoMapper);
    }

    @Test
    void shouldHandleNullProductsInOrder() {
        // Arrange
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .client(null)
                .products(null)
                .deliveryAddress(null)
                .total(null)
                .orderDate(LocalDateTime.now())
                .orderStatus("PENDING")
                .build();

        // Act
        OrderDto dto = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(dto);
        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
        verifyNoInteractions(productDtoMapper);
    }
} 