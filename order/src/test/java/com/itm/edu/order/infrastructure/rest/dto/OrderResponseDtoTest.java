package com.itm.edu.order.infrastructure.rest.dto;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderResponseDtoTest {

    @Test
    void shouldCreateOrderResponseDtoSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        LocalDateTime orderDate = LocalDateTime.now();
        String orderStatus = "PENDING";

        OrderResponseDto.ClientResponseDto clientDto = OrderResponseDto.ClientResponseDto.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        OrderResponseDto.ProductResponseDto productDto = OrderResponseDto.ProductResponseDto.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .price(99.99)
                .stock(100)
                .build();

        OrderResponseDto.OrderItemResponseDto orderItemDto = OrderResponseDto.OrderItemResponseDto.builder()
                .product(productDto)
                .quantity(2)
                .build();

        OrderResponseDto.AddressResponseDto addressDto = OrderResponseDto.AddressResponseDto.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        OrderResponseDto.MoneyResponseDto totalDto = OrderResponseDto.MoneyResponseDto.builder()
                .amount(new BigDecimal("199.98"))
                .currency("USD")
                .build();

        // Act
        OrderResponseDto responseDto = OrderResponseDto.builder()
                .orderId(orderId)
                .client(clientDto)
                .products(Arrays.asList(orderItemDto))
                .deliveryAddress(addressDto)
                .total(totalDto)
                .orderDate(orderDate)
                .orderStatus(orderStatus)
                .build();

        // Assert
        assertNotNull(responseDto);
        assertEquals(orderId, responseDto.getOrderId());
        assertEquals(clientDto, responseDto.getClient());
        assertEquals(1, responseDto.getProducts().size());
        assertEquals(orderItemDto, responseDto.getProducts().get(0));
        assertEquals(addressDto, responseDto.getDeliveryAddress());
        assertEquals(totalDto, responseDto.getTotal());
        assertEquals(orderDate, responseDto.getOrderDate());
        assertEquals(orderStatus, responseDto.getOrderStatus());
    }

    @Test
    void shouldCreateEmptyOrderResponseDto() {
        // Act
        OrderResponseDto responseDto = OrderResponseDto.builder().build();

        // Assert
        assertNotNull(responseDto);
        assertNull(responseDto.getOrderId());
        assertNull(responseDto.getClient());
        assertNull(responseDto.getProducts());
        assertNull(responseDto.getDeliveryAddress());
        assertNull(responseDto.getTotal());
        assertNull(responseDto.getOrderDate());
        assertNull(responseDto.getOrderStatus());
    }

    @Test
    void shouldCreateOrderItemResponseDtoSuccessfully() {
        // Arrange
        OrderResponseDto.ProductResponseDto productDto = OrderResponseDto.ProductResponseDto.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .price(99.99)
                .stock(100)
                .build();
        int quantity = 2;

        // Act
        OrderResponseDto.OrderItemResponseDto orderItemDto = OrderResponseDto.OrderItemResponseDto.builder()
                .product(productDto)
                .quantity(quantity)
                .build();

        // Assert
        assertNotNull(orderItemDto);
        assertEquals(productDto, orderItemDto.getProduct());
        assertEquals(quantity, orderItemDto.getQuantity());
    }

    @Test
    void shouldCreateProductResponseDtoSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        double price = 99.99;
        int stock = 100;

        // Act
        OrderResponseDto.ProductResponseDto productDto = OrderResponseDto.ProductResponseDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();

        // Assert
        assertNotNull(productDto);
        assertEquals(id, productDto.getId());
        assertEquals(name, productDto.getName());
        assertEquals(description, productDto.getDescription());
        assertEquals(price, productDto.getPrice());
        assertEquals(stock, productDto.getStock());
    }

    @Test
    void shouldMapFromDomainSuccessfully() {
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

        OrderItem orderItem = OrderItem.create(product, 2);

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

        // Act
        OrderResponseDto responseDto = OrderResponseDto.fromDomain(order);

        // Assert
        assertNotNull(responseDto);
        assertEquals(orderId, responseDto.getOrderId());
        
        // Verify client mapping
        assertNotNull(responseDto.getClient());
        assertEquals(client.getId(), responseDto.getClient().getId());
        assertEquals(client.getName(), responseDto.getClient().getName());
        assertEquals(client.getEmail(), responseDto.getClient().getEmail());
        assertEquals(client.getPhone(), responseDto.getClient().getPhone());

        // Verify products mapping
        assertNotNull(responseDto.getProducts());
        assertEquals(1, responseDto.getProducts().size());
        OrderResponseDto.OrderItemResponseDto mappedItem = responseDto.getProducts().get(0);
        assertEquals(orderItem.getQuantity(), mappedItem.getQuantity());
        assertNotNull(mappedItem.getProduct());
        assertEquals(product.getId(), mappedItem.getProduct().getId());
        assertEquals(product.getName(), mappedItem.getProduct().getName());
        assertEquals(product.getDescription(), mappedItem.getProduct().getDescription());
        assertEquals(product.getPrice().doubleValue(), mappedItem.getProduct().getPrice());
        assertEquals(product.getStock(), mappedItem.getProduct().getStock());

        // Verify address mapping
        assertNotNull(responseDto.getDeliveryAddress());
        assertEquals(deliveryAddress.getStreet(), responseDto.getDeliveryAddress().getStreet());
        assertEquals(deliveryAddress.getCity(), responseDto.getDeliveryAddress().getCity());
        assertEquals(deliveryAddress.getState(), responseDto.getDeliveryAddress().getState());
        assertEquals(deliveryAddress.getZipCode(), responseDto.getDeliveryAddress().getZipCode());
        assertEquals(deliveryAddress.getCountry(), responseDto.getDeliveryAddress().getCountry());

        // Verify total mapping
        assertNotNull(responseDto.getTotal());
        assertEquals(total.getAmount(), responseDto.getTotal().getAmount());
        assertEquals(total.getCurrency(), responseDto.getTotal().getCurrency());

        assertEquals(orderDate, responseDto.getOrderDate());
        assertEquals(orderStatus, responseDto.getOrderStatus());
    }
} 