package com.wilson.order.domain.model;

import com.wilson.order.domain.valueobjects.AddressShipping;
import com.wilson.order.domain.valueobjects.OrderTotalValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderGettersAndSetters() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String orderStatus = "PENDING";
        LocalDateTime orderDate = LocalDateTime.now();
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
        
        Order order = new Order();
        
        // Act
        order.setOrderId(orderId);
        order.setOrderStatus(orderStatus);
        order.setOrderDate(orderDate);
        order.setClient(client);
        
        // Assert
        assertEquals(orderId, order.getOrderId());
        assertEquals(orderStatus, order.getOrderStatus());
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(client, order.getClient());
    }

    @Test
    void testAddressShippingGetters() {
        // Arrange
        AddressShipping address = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();
        
        // Assert
        assertEquals("123 Main St", address.getStreet());
        assertEquals("New York", address.getCity());
        assertEquals("NY", address.getState());
        assertEquals("10001", address.getZipCode());
        assertEquals("USA", address.getCountry());
    }

    @Test
    void testOrderTotalValueGetters() {
        // Arrange
        BigDecimal amount = new BigDecimal("100.00");
        String currency = "USD";
        OrderTotalValue total = new OrderTotalValue(amount, currency);
        
        // Assert
        assertEquals(amount, total.getAmount());
        assertEquals(currency, total.getCurrency());
    }

    @Test
    void testOrderTotalValueValidation() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new OrderTotalValue(new BigDecimal("-100.00"), "USD");
        });
    }

    @Test
    void testOrderWithAddressShipping() {
        // Arrange
        AddressShipping address = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();
        
        Order order = new Order();
        
        // Act
        order.setDeliveryAddress(address);
        
        // Assert
        assertNotNull(order.getDeliveryAddress());
        assertEquals("123 Main St", order.getDeliveryAddress().getStreet());
        assertEquals("New York", order.getDeliveryAddress().getCity());
        assertEquals("NY", order.getDeliveryAddress().getState());
        assertEquals("10001", order.getDeliveryAddress().getZipCode());
        assertEquals("USA", order.getDeliveryAddress().getCountry());
    }

    @Test
    void testOrderWithTotalValue() {
        // Arrange
        OrderTotalValue total = new OrderTotalValue(new BigDecimal("100.00"), "USD");
        Order order = new Order();
        
        // Act
        order.setTotal(total);
        
        // Assert
        assertNotNull(order.getTotal());
        assertEquals(new BigDecimal("100.00"), order.getTotal().getAmount());
        assertEquals("USD", order.getTotal().getCurrency());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testAddressShippingWithInvalidStreet(String invalidStreet) {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AddressShipping.builder()
                    .street(invalidStreet)
                    .city("New York")
                    .state("NY")
                    .zipCode("10001")
                    .country("USA")
                    .build();
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void testAddressShippingWithInvalidCity(String invalidCity) {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            AddressShipping.builder()
                    .street("123 Main St")
                    .city(invalidCity)
                    .state("NY")
                    .zipCode("10001")
                    .country("USA")
                    .build();
        });
    }

    @Test
    void testOrderTotalValueWithZeroAmount() {
        // Arrange & Act
        OrderTotalValue total = new OrderTotalValue(BigDecimal.ZERO, "USD");
        
        // Assert
        assertEquals(BigDecimal.ZERO, total.getAmount());
        assertEquals("USD", total.getCurrency());
    }

    @Test
    void testOrderTotalValueWithDifferentCurrency() {
        // Arrange & Act
        OrderTotalValue total = new OrderTotalValue(new BigDecimal("100.00"), "EUR");
        
        // Assert
        assertEquals(new BigDecimal("100.00"), total.getAmount());
        assertEquals("EUR", total.getCurrency());
    }

    @Test
    void testCalculateTotalWithEmptyProducts() {
        // Arrange
        Order order = new Order();
        
        // Act
        order.calculateTotal();
        
        // Assert
        assertNotNull(order.getTotal());
        assertEquals(BigDecimal.ZERO, order.getTotal().getAmount());
        assertEquals("USD", order.getTotal().getCurrency());
    }

    @Test
    void testCalculateTotalWithSingleProduct() {
        // Arrange
        Order order = new Order();
        Product product = Product.builder()
                .price(new BigDecimal("10.00"))
                .build();
        
        // Act
        order.addProduct(product, new BigDecimal("2"));
        
        // Assert
        assertNotNull(order.getTotal());
        assertEquals(new BigDecimal("20.00"), order.getTotal().getAmount());
        assertEquals("USD", order.getTotal().getCurrency());
    }

    @Test
    void testCalculateTotalWithMultipleProducts() {
        // Arrange
        Order order = new Order();
        Product product1 = Product.builder()
                .price(new BigDecimal("10.00"))
                .build();
        Product product2 = Product.builder()
                .price(new BigDecimal("15.00"))
                .build();
        
        // Act
        order.addProduct(product1, new BigDecimal("2"));
        order.addProduct(product2, new BigDecimal("3"));
        
        // Assert
        assertNotNull(order.getTotal());
        assertEquals(new BigDecimal("65.00"), order.getTotal().getAmount());
        assertEquals("USD", order.getTotal().getCurrency());
    }

    @Test
    void testCalculateTotalAfterRemovingProduct() {
        // Arrange
        Order order = new Order();
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("10.00"))
                .build();
        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("15.00"))
                .build();
        
        // Act
        order.addProduct(product1, new BigDecimal("2"));
        order.addProduct(product2, new BigDecimal("3"));
        order.removeProduct(product1);
        
        // Assert
        assertNotNull(order.getTotal());
        assertEquals(new BigDecimal("45.00"), order.getTotal().getAmount());
        assertEquals("USD", order.getTotal().getCurrency());
    }

    @Test
    void testOrderWithClient() {
        // Arrange
        Client client = Client.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
        
        Order order = new Order();
        
        // Act
        order.setClient(client);
        
        // Assert
        assertNotNull(order.getClient());
        assertEquals("John Doe", order.getClient().getName());
        assertEquals("john.doe@example.com", order.getClient().getEmail());
        assertEquals("1234567890", order.getClient().getPhone());
    }
} 