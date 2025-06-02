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
    void shouldCreateOrderResponseDtoWithAllFields() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        LocalDateTime orderDate = LocalDateTime.now();
        String orderStatus = "PENDING";

        // Act
        OrderResponseDto dto = OrderResponseDto.builder()
                .orderId(orderId)
                .client(OrderResponseDto.ClientResponseDto.builder()
                        .id(UUID.randomUUID())
                        .name("John Doe")
                        .email("john@example.com")
                        .phone("1234567890")
                        .build())
                .products(java.util.Arrays.asList(
                        OrderResponseDto.OrderItemResponseDto.builder()
                                .product(OrderResponseDto.ProductResponseDto.builder()
                                        .id(UUID.randomUUID())
                                        .name("Product 1")
                                        .description("Description 1")
                                        .price(99.99)
                                        .stock(100)
                                        .build())
                                .quantity(2)
                                .build()
                ))
                .deliveryAddress(OrderResponseDto.AddressResponseDto.builder()
                        .street("123 Main St")
                        .city("Test City")
                        .state("Test State")
                        .zipCode("12345")
                        .country("Test Country")
                        .build())
                .total(OrderResponseDto.MoneyResponseDto.builder()
                        .amount(new BigDecimal("199.98"))
                        .currency("USD")
                        .build())
                .orderDate(orderDate)
                .orderStatus(orderStatus)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(orderId, dto.getOrderId());
        assertNotNull(dto.getClient());
        assertNotNull(dto.getProducts());
        assertEquals(1, dto.getProducts().size());
        assertNotNull(dto.getDeliveryAddress());
        assertNotNull(dto.getTotal());
        assertEquals(orderDate, dto.getOrderDate());
        assertEquals(orderStatus, dto.getOrderStatus());
    }

    @Test
    void shouldCreateOrderResponseDtoWithNullFields() {
        // Act
        OrderResponseDto dto = OrderResponseDto.builder()
                .orderId(UUID.randomUUID())
                .client(null)
                .products(null)
                .deliveryAddress(null)
                .total(null)
                .orderDate(LocalDateTime.now())
                .orderStatus("PENDING")
                .build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getClient());
        assertNull(dto.getProducts());
        assertNull(dto.getDeliveryAddress());
        assertNull(dto.getTotal());
    }

    @Test
    void shouldCreateClientResponseDtoWithAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        String email = "john@example.com";
        String phone = "1234567890";

        // Act
        OrderResponseDto.ClientResponseDto dto = OrderResponseDto.ClientResponseDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(email, dto.getEmail());
        assertEquals(phone, dto.getPhone());
    }

    @Test
    void shouldCreateOrderItemResponseDtoWithAllFields() {
        // Arrange
        OrderResponseDto.ProductResponseDto product = OrderResponseDto.ProductResponseDto.builder()
                .id(UUID.randomUUID())
                .name("Product 1")
                .description("Description 1")
                .price(99.99)
                .stock(100)
                .build();
        int quantity = 2;

        // Act
        OrderResponseDto.OrderItemResponseDto dto = OrderResponseDto.OrderItemResponseDto.builder()
                .product(product)
                .quantity(quantity)
                .build();

        // Assert
        assertNotNull(dto);
        assertNotNull(dto.getProduct());
        assertEquals(product.getId(), dto.getProduct().getId());
        assertEquals(quantity, dto.getQuantity());
    }

    @Test
    void shouldCreateProductResponseDtoWithAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Product 1";
        String description = "Description 1";
        double price = 99.99;
        int stock = 100;

        // Act
        OrderResponseDto.ProductResponseDto dto = OrderResponseDto.ProductResponseDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(description, dto.getDescription());
        assertEquals(price, dto.getPrice());
        assertEquals(stock, dto.getStock());
    }

    @Test
    void shouldCreateAddressResponseDtoWithAllFields() {
        // Arrange
        String street = "123 Main St";
        String city = "Test City";
        String state = "Test State";
        String zipCode = "12345";
        String country = "Test Country";

        // Act
        OrderResponseDto.AddressResponseDto dto = OrderResponseDto.AddressResponseDto.builder()
                .street(street)
                .city(city)
                .state(state)
                .zipCode(zipCode)
                .country(country)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(street, dto.getStreet());
        assertEquals(city, dto.getCity());
        assertEquals(state, dto.getState());
        assertEquals(zipCode, dto.getZipCode());
        assertEquals(country, dto.getCountry());
    }

    @Test
    void shouldCreateMoneyResponseDtoWithAllFields() {
        // Arrange
        BigDecimal amount = new BigDecimal("199.98");
        String currency = "USD";

        // Act
        OrderResponseDto.MoneyResponseDto dto = OrderResponseDto.MoneyResponseDto.builder()
                .amount(amount)
                .currency(currency)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(amount, dto.getAmount());
        assertEquals(currency, dto.getCurrency());
    }

    @Test
    void shouldCreateOrderItemResponseDtoWithNullProduct() {
        // Act
        OrderResponseDto.OrderItemResponseDto dto = OrderResponseDto.OrderItemResponseDto.builder()
                .product(null)
                .quantity(2)
                .build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getProduct());
        assertEquals(2, dto.getQuantity());
    }

    @Test
    void shouldCreateProductResponseDtoWithNullFields() {
        // Act
        OrderResponseDto.ProductResponseDto dto = OrderResponseDto.ProductResponseDto.builder()
                .id(null)
                .name(null)
                .description(null)
                .price(0.0)
                .stock(0)
                .build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getDescription());
        assertEquals(0.0, dto.getPrice());
        assertEquals(0, dto.getStock());
    }

    @Test
    void shouldCreateAddressResponseDtoWithNullFields() {
        // Act
        OrderResponseDto.AddressResponseDto dto = OrderResponseDto.AddressResponseDto.builder()
                .street(null)
                .city(null)
                .state(null)
                .zipCode(null)
                .country(null)
                .build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getStreet());
        assertNull(dto.getCity());
        assertNull(dto.getState());
        assertNull(dto.getZipCode());
        assertNull(dto.getCountry());
    }

    @Test
    void shouldCreateMoneyResponseDtoWithNullFields() {
        // Act
        OrderResponseDto.MoneyResponseDto dto = OrderResponseDto.MoneyResponseDto.builder()
                .amount(null)
                .currency(null)
                .build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getAmount());
        assertNull(dto.getCurrency());
    }

    @Test
    void shouldCreateClientResponseDtoWithNullFields() {
        // Act
        OrderResponseDto.ClientResponseDto dto = OrderResponseDto.ClientResponseDto.builder()
                .id(null)
                .name(null)
                .email(null)
                .phone(null)
                .build();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getPhone());
    }

    @Test
    void shouldConvertFromDomainToDto() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        LocalDateTime orderDate = LocalDateTime.now();
        
        Client client = Client.builder()
                .id(clientId)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        Product product = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        OrderItem orderItem = OrderItem.create(product, 2);

        AddressShipping address = AddressShipping.builder()
                .street("123 Main St")
                .city("Test City")
                .state("Test State")
                .zipCode("12345")
                .country("Test Country")
                .build();

        Order order = Order.builder()
                .orderId(orderId)
                .client(client)
                .products(Arrays.asList(orderItem))
                .deliveryAddress(address)
                .total(new OrderTotalValue(new BigDecimal("199.98"), "USD"))
                .orderDate(orderDate)
                .orderStatus("PENDING")
                .build();

        // Act
        OrderResponseDto dto = OrderResponseDto.fromDomain(order);

        // Assert
        assertNotNull(dto);
        assertEquals(orderId, dto.getOrderId());
        assertNotNull(dto.getClient());
        assertEquals(clientId, dto.getClient().getId());
        assertEquals("John Doe", dto.getClient().getName());
        assertEquals("john@example.com", dto.getClient().getEmail());
        assertEquals("1234567890", dto.getClient().getPhone());
        
        assertNotNull(dto.getProducts());
        assertEquals(1, dto.getProducts().size());
        OrderResponseDto.OrderItemResponseDto itemDto = dto.getProducts().get(0);
        assertEquals(2, itemDto.getQuantity());
        assertNotNull(itemDto.getProduct());
        assertEquals(productId, itemDto.getProduct().getId());
        assertEquals("Test Product", itemDto.getProduct().getName());
        assertEquals("Test Description", itemDto.getProduct().getDescription());
        assertEquals(99.99, itemDto.getProduct().getPrice());
        assertEquals(100, itemDto.getProduct().getStock());
        
        assertNotNull(dto.getDeliveryAddress());
        assertEquals("123 Main St", dto.getDeliveryAddress().getStreet());
        assertEquals("Test City", dto.getDeliveryAddress().getCity());
        assertEquals("Test State", dto.getDeliveryAddress().getState());
        assertEquals("12345", dto.getDeliveryAddress().getZipCode());
        assertEquals("Test Country", dto.getDeliveryAddress().getCountry());
        
        assertNotNull(dto.getTotal());
        assertEquals(new BigDecimal("199.98"), dto.getTotal().getAmount());
        assertEquals("USD", dto.getTotal().getCurrency());
        
        assertEquals(orderDate, dto.getOrderDate());
        assertEquals("PENDING", dto.getOrderStatus());
    }

    @Test
    void shouldReturnNullWhenConvertingNullOrder() {
        // Act
        OrderResponseDto dto = OrderResponseDto.fromDomain(null);

        // Assert
        assertNull(dto);
    }
} 