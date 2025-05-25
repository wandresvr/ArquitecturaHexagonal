package com.itm.edu.order.infrastructure.rest.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class OrderDtoTest {

    @Test
    void shouldCreateOrderDtoSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        LocalDateTime orderDate = LocalDateTime.now();
        String orderStatus = "PENDING";

        OrderDto.ClientDto clientDto = OrderDto.ClientDto.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        OrderDto.ProductDto productDto = OrderDto.ProductDto.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        OrderDto.OrderItemDto orderItemDto = OrderDto.OrderItemDto.builder()
                .id(UUID.randomUUID())
                .product(productDto)
                .quantity(2)
                .build();

        OrderDto.AddressDto addressDto = OrderDto.AddressDto.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        OrderDto.MoneyDto totalDto = OrderDto.MoneyDto.builder()
                .amount(new BigDecimal("199.98"))
                .currency("USD")
                .build();

        // Act
        OrderDto orderDto = OrderDto.builder()
                .orderId(orderId)
                .client(clientDto)
                .items(Arrays.asList(orderItemDto))
                .shippingAddress(addressDto)
                .total(totalDto)
                .orderDate(orderDate)
                .orderStatus(orderStatus)
                .build();

        // Assert
        assertNotNull(orderDto);
        assertEquals(orderId, orderDto.getOrderId());
        assertEquals(clientDto, orderDto.getClient());
        assertEquals(1, orderDto.getItems().size());
        assertEquals(orderItemDto, orderDto.getItems().get(0));
        assertEquals(addressDto, orderDto.getShippingAddress());
        assertEquals(totalDto, orderDto.getTotal());
        assertEquals(orderDate, orderDto.getOrderDate());
        assertEquals(orderStatus, orderDto.getOrderStatus());
    }

    @Test
    void shouldCreateEmptyOrderDto() {
        // Act
        OrderDto orderDto = OrderDto.builder().build();

        // Assert
        assertNotNull(orderDto);
        assertNull(orderDto.getOrderId());
        assertNull(orderDto.getClient());
        assertNull(orderDto.getItems());
        assertNull(orderDto.getShippingAddress());
        assertNull(orderDto.getTotal());
        assertNull(orderDto.getOrderDate());
        assertNull(orderDto.getOrderStatus());
    }

    @Test
    void shouldCreateClientDtoSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        String email = "john@example.com";
        String phone = "1234567890";

        // Act
        OrderDto.ClientDto clientDto = OrderDto.ClientDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .phone(phone)
                .build();

        // Assert
        assertNotNull(clientDto);
        assertEquals(id, clientDto.getId());
        assertEquals(name, clientDto.getName());
        assertEquals(email, clientDto.getEmail());
        assertEquals(phone, clientDto.getPhone());
    }

    @Test
    void shouldCreateProductDtoSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("99.99");
        Integer stock = 100;

        // Act
        OrderDto.ProductDto productDto = OrderDto.ProductDto.builder()
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
    void shouldCreateOrderItemDtoSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        OrderDto.ProductDto productDto = OrderDto.ProductDto.builder().build();
        int quantity = 2;

        // Act
        OrderDto.OrderItemDto orderItemDto = OrderDto.OrderItemDto.builder()
                .id(id)
                .product(productDto)
                .quantity(quantity)
                .build();

        // Assert
        assertNotNull(orderItemDto);
        assertEquals(id, orderItemDto.getId());
        assertEquals(productDto, orderItemDto.getProduct());
        assertEquals(quantity, orderItemDto.getQuantity());
    }

    @Test
    void shouldCreateAddressDtoSuccessfully() {
        // Arrange
        String street = "123 Main St";
        String city = "New York";
        String state = "NY";
        String zipCode = "10001";
        String country = "USA";

        // Act
        OrderDto.AddressDto addressDto = OrderDto.AddressDto.builder()
                .street(street)
                .city(city)
                .state(state)
                .zipCode(zipCode)
                .country(country)
                .build();

        // Assert
        assertNotNull(addressDto);
        assertEquals(street, addressDto.getStreet());
        assertEquals(city, addressDto.getCity());
        assertEquals(state, addressDto.getState());
        assertEquals(zipCode, addressDto.getZipCode());
        assertEquals(country, addressDto.getCountry());
    }

    @Test
    void shouldCreateMoneyDtoSuccessfully() {
        // Arrange
        BigDecimal amount = new BigDecimal("199.98");
        String currency = "USD";

        // Act
        OrderDto.MoneyDto moneyDto = OrderDto.MoneyDto.builder()
                .amount(amount)
                .currency(currency)
                .build();

        // Assert
        assertNotNull(moneyDto);
        assertEquals(amount, moneyDto.getAmount());
        assertEquals(currency, moneyDto.getCurrency());
    }
} 