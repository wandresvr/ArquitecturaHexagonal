package com.itm.edu.order.domain.model;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        AddressShipping address = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        // Act
        Order order = Order.builder()
                .orderId(orderId)
                .client(client)
                .orderStatus("PENDING")
                .orderDate(LocalDateTime.now())
                .deliveryAddress(address)
                .products(new ArrayList<>())
                .total(new OrderTotalValue(BigDecimal.ZERO, "USD"))
                .build();

        // Assert
        assertNotNull(order);
        assertEquals(orderId, order.getOrderId());
        assertEquals("PENDING", order.getOrderStatus());
        assertEquals(client, order.getClient());
        assertEquals(address, order.getDeliveryAddress());
        assertTrue(order.getProducts().isEmpty());
    }

    @Test
    void shouldUpdateStatusSuccessfully() {
        // Arrange
        Order order = createTestOrder();
        String newStatus = "COMPLETED";

        // Act
        Order updatedOrder = order.withUpdatedStatus(newStatus);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(newStatus, updatedOrder.getOrderStatus());
        assertEquals(order.getOrderId(), updatedOrder.getOrderId());
        assertEquals(order.getClient(), updatedOrder.getClient());
        assertEquals(order.getDeliveryAddress(), updatedOrder.getDeliveryAddress());
    }

    @Test
    void shouldUpdateDeliveryAddressSuccessfully() {
        // Arrange
        Order order = createTestOrder();
        AddressShipping newAddress = AddressShipping.builder()
                .street("456 New St")
                .city("Los Angeles")
                .state("CA")
                .zipCode("90001")
                .country("USA")
                .build();

        // Act
        Order updatedOrder = order.withUpdatedDeliveryAddress(newAddress);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(newAddress, updatedOrder.getDeliveryAddress());
        assertEquals(order.getOrderId(), updatedOrder.getOrderId());
        assertEquals(order.getOrderStatus(), updatedOrder.getOrderStatus());
        assertEquals(order.getClient(), updatedOrder.getClient());
    }

    @Test
    void shouldAddProductSuccessfully() {
        // Arrange
        Order order = createTestOrder();
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        // Act
        Order updatedOrder = order.withAddedProduct(product, 2);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(1, updatedOrder.getProducts().size());
        OrderItem addedItem = updatedOrder.getProducts().get(0);
        assertEquals(product, addedItem.getProduct());
        assertEquals(2, addedItem.getQuantity());
    }

    @Test
    void shouldCalculateTotalCorrectly() {
        // Arrange
        Order order = createTestOrder();
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 1")
                .description("Description 1")
                .price(new BigDecimal("100.00"))
                .stock(100)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 2")
                .description("Description 2")
                .price(new BigDecimal("50.00"))
                .stock(100)
                .build();

        // Act
        Order updatedOrder = order
                .withAddedProduct(product1, 2)
                .withAddedProduct(product2, 3);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(2, updatedOrder.getProducts().size());
        assertEquals(new BigDecimal("350.00"), updatedOrder.getTotal().getAmount());
    }

    @Test
    void shouldRemoveProductSuccessfully() {
        // Arrange
        Order order = createTestOrder();
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 1")
                .description("Description 1")
                .price(new BigDecimal("100.00"))
                .stock(100)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 2")
                .description("Description 2")
                .price(new BigDecimal("50.00"))
                .stock(100)
                .build();

        order = order
                .withAddedProduct(product1, 2)
                .withAddedProduct(product2, 3);

        // Act
        Order updatedOrder = order.withRemovedProduct(product1);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals(1, updatedOrder.getProducts().size());
        assertEquals(product2, updatedOrder.getProducts().get(0).getProduct());
    }

    private Order createTestOrder() {
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        AddressShipping address = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        return Order.builder()
                .orderId(UUID.randomUUID())
                .client(client)
                .orderStatus("PENDING")
                .orderDate(LocalDateTime.now())
                .deliveryAddress(address)
                .products(new ArrayList<>())
                .total(new OrderTotalValue(BigDecimal.ZERO, "USD"))
                .build();
    }

    @Test
    void testOrderGettersAndSetters() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String orderStatus = "PENDING";
        LocalDateTime orderDate = LocalDateTime.now();
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
        
        AddressShipping address = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        // Act
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(orderStatus)
                .orderDate(orderDate)
                .client(client)
                .deliveryAddress(address)
                .products(new ArrayList<>())
                .total(new OrderTotalValue(BigDecimal.ZERO, "USD"))
                .build();
        
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
    void testOrderWithDeliveryAddress() {
        // Arrange
        AddressShipping address = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        // Act
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .client(Client.builder()
                        .id(UUID.randomUUID())
                        .name("John Doe")
                        .email("john@example.com")
                        .phone("1234567890")
                        .build())
                .orderStatus("PENDING")
                .orderDate(LocalDateTime.now())
                .deliveryAddress(address)
                .products(new ArrayList<>())
                .total(new OrderTotalValue(BigDecimal.ZERO, "USD"))
                .build();
        
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
        
        // Act
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .client(Client.builder()
                        .id(UUID.randomUUID())
                        .name("John Doe")
                        .email("john@example.com")
                        .phone("1234567890")
                        .build())
                .orderStatus("PENDING")
                .orderDate(LocalDateTime.now())
                .deliveryAddress(AddressShipping.builder()
                        .street("123 Main St")
                        .city("New York")
                        .state("NY")
                        .zipCode("10001")
                        .country("USA")
                        .build())
                .products(new ArrayList<>())
                .total(total)
                .build();
        
        // Assert
        assertNotNull(order.getTotal());
        assertEquals(new BigDecimal("100.00"), order.getTotal().getAmount());
        assertEquals("USD", order.getTotal().getCurrency());
    }

    @Test
    void testOrderWithClient() {
        // Arrange
        Client client = Client.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
        
        // Act
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .client(client)
                .orderStatus("PENDING")
                .orderDate(LocalDateTime.now())
                .deliveryAddress(AddressShipping.builder()
                        .street("123 Main St")
                        .city("New York")
                        .state("NY")
                        .zipCode("10001")
                        .country("USA")
                        .build())
                .products(new ArrayList<>())
                .total(new OrderTotalValue(BigDecimal.ZERO, "USD"))
                .build();
        
        // Assert
        assertNotNull(order.getClient());
        assertEquals("John Doe", order.getClient().getName());
        assertEquals("john.doe@example.com", order.getClient().getEmail());
        assertEquals("1234567890", order.getClient().getPhone());
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
        Order order = Order.builder().build();

        // Act
        assertNotNull(order.getTotal(), "Total should be initialized even with no products");
        assertEquals(BigDecimal.ZERO, order.getTotal().getAmount());
    }

    @Test
    void testCalculateTotalWithSingleProduct() {
        // Arrange
        Order order = createTestOrder();
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("10.00"))
                .stock(100)
                .build();
        
        // Act
        Order updatedOrder = order.withAddedProduct(product, 2);
        
        // Assert
        assertNotNull(updatedOrder.getTotal());
        assertEquals(new BigDecimal("20.00"), updatedOrder.getTotal().getAmount());
    }

    @Test
    void testCalculateTotalWithMultipleProducts() {
        // Arrange
        Order order = createTestOrder();
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("10.00"))
                .name("Product 1")
                .description("Description 1")
                .stock(100)
                .build();
        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("15.00"))
                .name("Product 2")
                .description("Description 2")
                .stock(100)
                .build();
        
        // Act
        Order updatedOrder = order
                .withAddedProduct(product1, 2)
                .withAddedProduct(product2, 3);
        
        // Assert
        assertNotNull(updatedOrder.getTotal());
        assertEquals(new BigDecimal("65.00"), updatedOrder.getTotal().getAmount());
    }

    @Test
    void testCalculateTotalAfterRemovingProduct() {
        // Arrange
        Order order = createTestOrder();
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("10.00"))
                .name("Product 1")
                .description("Description 1")
                .stock(100)
                .build();
        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("15.00"))
                .name("Product 2")
                .description("Description 2")
                .stock(100)
                .build();
        
        // Act
        Order updatedOrder = order
                .withAddedProduct(product1, 2)
                .withAddedProduct(product2, 3)
                .withRemovedProduct(product1);
        
        // Assert
        assertNotNull(updatedOrder.getTotal());
        assertEquals(new BigDecimal("45.00"), updatedOrder.getTotal().getAmount());
    }
} 