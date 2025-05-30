package com.itm.edu.order.infrastructure.persistence.mapper;

import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import com.itm.edu.order.infrastructure.persistence.entities.OrderEntity;
import com.itm.edu.order.infrastructure.persistence.entities.ClientEntity;
import com.itm.edu.order.infrastructure.persistence.entities.OrderItemEntity;
import com.itm.edu.order.infrastructure.persistence.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private OrderMapper orderMapper;

    private UUID orderId;
    private LocalDateTime orderDate;
    private AddressShipping addressShipping;
    private Client client;
    private ClientEntity clientEntity;
    private OrderItem orderItem;
    private OrderItemEntity orderItemEntity;
    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        orderDate = LocalDateTime.now();
        
        addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("Test City")
                .state("Test State")
                .zipCode("12345")
                .country("Test Country")
                .build();

        client = Client.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        clientEntity = ClientEntity.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .build();

        product = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("10.99"))
                .stock(100)
                .build();

        productEntity = ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();

        orderItem = OrderItem.create(product, 2);

        orderItemEntity = OrderItemEntity.builder()
                .product(productEntity)
                .quantity(orderItem.getQuantity())
                .build();
    }

    @Test
    void toDomain_Success() {
        // Arrange
        OrderEntity entity = OrderEntity.builder()
                .orderId(orderId)
                .orderStatus("PENDING")
                .orderDate(orderDate)
                .deliveryAddress(addressShipping)
                .total(new OrderTotalValue(new BigDecimal("21.98"), "USD"))
                .client(clientEntity)
                .products(Arrays.asList(orderItemEntity))
                .build();

        when(clientMapper.toDomain(clientEntity)).thenReturn(client);
        when(orderItemMapper.toDomain(orderItemEntity)).thenReturn(orderItem);

        // Act
        Order result = orderMapper.toDomain(entity);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals("PENDING", result.getOrderStatus());
        assertEquals(orderDate, result.getOrderDate());
        assertEquals(addressShipping, result.getDeliveryAddress());
        assertEquals(new BigDecimal("21.98"), result.getTotal().getAmount());
        assertEquals("USD", result.getTotal().getCurrency());
        assertEquals(client, result.getClient());
        assertEquals(1, result.getProducts().size());
        assertEquals(orderItem, result.getProducts().get(0));

        verify(clientMapper).toDomain(clientEntity);
        verify(orderItemMapper).toDomain(orderItemEntity);
    }

    @Test
    void toDomain_NullEntity() {
        // Act
        Order result = orderMapper.toDomain(null);

        // Assert
        assertNull(result);
        verifyNoInteractions(clientMapper, orderItemMapper);
    }

    @Test
    void toEntity_Success() {
        // Arrange
        Order domain = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING")
                .orderDate(orderDate)
                .deliveryAddress(addressShipping)
                .total(new OrderTotalValue(new BigDecimal("21.98"), "USD"))
                .client(client)
                .products(Arrays.asList(orderItem))
                .build();

        when(clientMapper.toEntity(client)).thenReturn(clientEntity);
        when(orderItemMapper.toEntity(orderItem)).thenReturn(orderItemEntity);

        // Act
        OrderEntity result = orderMapper.toEntity(domain);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals("PENDING", result.getOrderStatus());
        assertEquals(orderDate, result.getOrderDate());
        assertEquals(addressShipping, result.getDeliveryAddress());
        assertEquals(new BigDecimal("21.98"), result.getTotal().getAmount());
        assertEquals("USD", result.getTotal().getCurrency());
        assertEquals(clientEntity, result.getClient());
        assertEquals(1, result.getProducts().size());
        assertEquals(orderItemEntity, result.getProducts().get(0));
        assertEquals(result, result.getProducts().get(0).getOrder()); // Verifica la relación bidireccional

        verify(clientMapper).toEntity(client);
        verify(orderItemMapper).toEntity(orderItem);
    }

    @Test
    void toEntity_NullDomain() {
        // Act
        OrderEntity result = orderMapper.toEntity(null);

        // Assert
        assertNull(result);
        verifyNoInteractions(clientMapper, orderItemMapper);
    }
} 