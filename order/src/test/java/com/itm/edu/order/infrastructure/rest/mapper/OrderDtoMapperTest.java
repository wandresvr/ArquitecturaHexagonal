package com.itm.edu.order.infrastructure.rest.mapper;

import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import com.itm.edu.order.infrastructure.rest.dto.OrderDto;
import com.itm.edu.order.infrastructure.rest.dto.ProductDto;
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
class OrderDtoMapperTest {

    @Mock
    private ProductDtoMapper productDtoMapper;

    @InjectMocks
    private OrderDtoMapper orderDtoMapper;

    private Order order;
    private Client client;
    private Product product;
    private OrderItem orderItem;
    private AddressShipping address;
    private OrderTotalValue total;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        LocalDateTime orderDate = LocalDateTime.now();

        client = Client.builder()
                .id(clientId)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        orderItem = OrderItem.create(product, 2);

        address = AddressShipping.builder()
                .street("123 Main St")
                .city("Test City")
                .state("Test State")
                .zipCode("12345")
                .country("Test Country")
                .build();

        total = new OrderTotalValue(new BigDecimal("199.98"), "USD");

        order = Order.builder()
                .orderId(orderId)
                .client(client)
                .products(Arrays.asList(orderItem))
                .deliveryAddress(address)
                .total(total)
                .orderDate(orderDate)
                .orderStatus("PENDING")
                .build();

        productDto = ProductDto.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();
    }

    @Test
    void toDto_ShouldMapOrderWithAllFields() {
        // Arrange
        when(productDtoMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act
        OrderDto result = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.getOrderId(), result.getOrderId());
        assertEquals(order.getOrderDate(), result.getOrderDate());
        assertEquals(order.getOrderStatus(), result.getOrderStatus());

        // Verify client mapping
        assertNotNull(result.getClient());
        assertEquals(client.getId(), result.getClient().getId());
        assertEquals(client.getName(), result.getClient().getName());
        assertEquals(client.getEmail(), result.getClient().getEmail());
        assertEquals(client.getPhone(), result.getClient().getPhone());

        // Verify products mapping
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
        OrderDto.OrderItemDto mappedItem = result.getItems().get(0);
        assertEquals(orderItem.getQuantity(), mappedItem.getQuantity());
        assertNotNull(mappedItem.getProduct());
        assertEquals(product.getId(), mappedItem.getProduct().getId());
        assertEquals(product.getName(), mappedItem.getProduct().getName());
        assertEquals(product.getDescription(), mappedItem.getProduct().getDescription());
        assertEquals(product.getPrice(), mappedItem.getProduct().getPrice());
        assertEquals(product.getStock(), mappedItem.getProduct().getStock());

        // Verify address mapping
        assertNotNull(result.getShippingAddress());
        assertEquals(address.getStreet(), result.getShippingAddress().getStreet());
        assertEquals(address.getCity(), result.getShippingAddress().getCity());
        assertEquals(address.getState(), result.getShippingAddress().getState());
        assertEquals(address.getZipCode(), result.getShippingAddress().getZipCode());
        assertEquals(address.getCountry(), result.getShippingAddress().getCountry());

        // Verify total mapping
        assertNotNull(result.getTotal());
        assertEquals(total.getAmount(), result.getTotal().getAmount());
        assertEquals(total.getCurrency(), result.getTotal().getCurrency());

        verify(productDtoMapper).toDto(product);
    }

    @Test
    void toDto_WithNullOrder_ShouldReturnNull() {
        // Act
        OrderDto result = orderDtoMapper.toDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDto_WithNullClient_ShouldHandleGracefully() {
        // Arrange
        order = Order.builder()
                .orderId(UUID.randomUUID())
                .client(null)
                .orderStatus("PENDING")
                .build();

        // Act
        OrderDto result = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(result);
        assertNull(result.getClient());
    }

    @Test
    void toDto_WithNullProducts_ShouldHandleGracefully() {
        // Arrange
        order = Order.builder()
                .orderId(UUID.randomUUID())
                .products(null)
                .orderStatus("PENDING")
                .build();

        // Act
        OrderDto result = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(result);
        assertNull(result.getItems());
    }

    @Test
    void toDto_WithEmptyProductsList_ShouldHandleGracefully() {
        // Arrange
        order = Order.builder()
                .orderId(UUID.randomUUID())
                .products(Arrays.asList())
                .orderStatus("PENDING")
                .build();

        // Act
        OrderDto result = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(result);
        assertNull(result.getItems());
    }

    @Test
    void toDto_WithNullDeliveryAddress_ShouldHandleGracefully() {
        // Arrange
        order = Order.builder()
                .orderId(UUID.randomUUID())
                .deliveryAddress(null)
                .orderStatus("PENDING")
                .build();

        // Act
        OrderDto result = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(result);
        assertNull(result.getShippingAddress());
    }

    @Test
    void toDto_WithNullTotal_ShouldHandleGracefully() {
        // Arrange
        order = Order.builder()
                .orderId(UUID.randomUUID())
                .total(null)
                .orderStatus("PENDING")
                .build();

        // Act
        OrderDto result = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getTotal());
        assertEquals(BigDecimal.ZERO, result.getTotal().getAmount());
        assertEquals("COP", result.getTotal().getCurrency());
    }

    @Test
    void toDto_WithNullProductInOrderItem_ShouldHandleGracefully() {
        // Arrange
        OrderItem itemWithNullProduct = OrderItem.builder()
                .product(null)
                .quantity(2)
                .build();

        order = Order.builder()
                .orderId(UUID.randomUUID())
                .products(Arrays.asList(itemWithNullProduct))
                .orderStatus("PENDING")
                .build();

        // Act
        OrderDto result = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
        assertNull(result.getItems().get(0).getProduct());
        assertEquals(2, result.getItems().get(0).getQuantity());
    }

    @Test
    void toDto_WithMultipleProducts_ShouldMapAllProducts() {
        // Arrange
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 1")
                .description("Description 1")
                .price(new BigDecimal("10.00"))
                .stock(100)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 2")
                .description("Description 2")
                .price(new BigDecimal("20.00"))
                .stock(200)
                .build();

        OrderItem item1 = OrderItem.create(product1, 2);
        OrderItem item2 = OrderItem.create(product2, 3);

        order = Order.builder()
                .orderId(UUID.randomUUID())
                .products(Arrays.asList(item1, item2))
                .orderStatus("PENDING")
                .build();

        ProductDto productDto1 = ProductDto.builder()
                .id(product1.getId())
                .name("Product 1")
                .description("Description 1")
                .price(new BigDecimal("10.00"))
                .stock(100)
                .build();

        ProductDto productDto2 = ProductDto.builder()
                .id(product2.getId())
                .name("Product 2")
                .description("Description 2")
                .price(new BigDecimal("20.00"))
                .stock(200)
                .build();

        when(productDtoMapper.toDto(product1)).thenReturn(productDto1);
        when(productDtoMapper.toDto(product2)).thenReturn(productDto2);

        // Act
        OrderDto result = orderDtoMapper.toDto(order);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getItems());
        assertEquals(2, result.getItems().size());

        OrderDto.OrderItemDto firstItem = result.getItems().get(0);
        assertEquals(2, firstItem.getQuantity());
        assertNotNull(firstItem.getProduct());
        assertEquals(product1.getId(), firstItem.getProduct().getId());
        assertEquals(product1.getName(), firstItem.getProduct().getName());

        OrderDto.OrderItemDto secondItem = result.getItems().get(1);
        assertEquals(3, secondItem.getQuantity());
        assertNotNull(secondItem.getProduct());
        assertEquals(product2.getId(), secondItem.getProduct().getId());
        assertEquals(product2.getName(), secondItem.getProduct().getName());

        verify(productDtoMapper).toDto(product1);
        verify(productDtoMapper).toDto(product2);
    }
} 