package com.itm.edu.order.domain.model;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private UUID orderId;
    private Client client;
    private AddressShipping deliveryAddress;
    private Order order;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        client = Client.builder()
            .id(clientId)
            .name("John Doe")
            .email("john@example.com")
            .phone("1234567890")
            .build();

        deliveryAddress = AddressShipping.builder()
            .street("123 Main St")
            .city("Medellín")
            .state("Antioquia")
            .zipCode("050001")
            .country("Colombia")
            .build();

        product = Product.builder()
            .id(productId)
            .name("Test Product")
            .description("Test Description")
            .price(new BigDecimal("99.99"))
            .stock(10)
            .build();

        orderItem = OrderItem.builder()
            .product(product)
            .quantity(2)
            .build();

        order = Order.builder()
            .orderId(orderId)
            .client(client)
            .orderStatus("PENDING")
            .orderDate(LocalDateTime.now())
            .deliveryAddress(deliveryAddress)
            .products(new ArrayList<>(Collections.singletonList(orderItem)))
            .build();
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Act
        Order newOrder = Order.create(client, Collections.singletonList(orderItem), deliveryAddress);

        // Assert
        assertNotNull(newOrder);
        assertNotNull(newOrder.getOrderId());
        assertEquals(client, newOrder.getClient());
        assertEquals("PENDING_VALIDATION", newOrder.getOrderStatus());
        assertNotNull(newOrder.getOrderDate());
        assertEquals(deliveryAddress, newOrder.getDeliveryAddress());
        assertEquals(1, newOrder.getProducts().size());
        assertEquals(orderItem, newOrder.getProducts().get(0));
        assertNotNull(newOrder.getTotal());
    }

    @Test
    void shouldUpdateStatusSuccessfully() {
        // Act
        order.updateStatus("PROCESSING");

        // Assert
        assertEquals("PROCESSING", order.getOrderStatus());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionWhenUpdatingStatusWithInvalidValue(String invalidStatus) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> order.updateStatus(invalidStatus));
    }

    @Test
    void shouldAddProductSuccessfully() {
        // Arrange
        Product newProduct = Product.builder()
            .id(UUID.randomUUID())
            .name("New Product")
            .description("New Description")
            .price(new BigDecimal("149.99"))
            .stock(5)
            .build();

        // Act
        order.addProduct(newProduct, 3);

        // Assert
        assertEquals(2, order.getProducts().size());
        OrderItem addedItem = order.getProducts().get(1);
        assertEquals(newProduct, addedItem.getProduct());
        assertEquals(3, addedItem.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullProduct() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> order.addProduct(null, 1));
    }

    @Test
    void shouldThrowExceptionWhenAddingProductWithInvalidQuantity() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> order.addProduct(product, 0));
        assertThrows(IllegalArgumentException.class, () -> order.addProduct(product, -1));
    }

    @Test
    void shouldCreateOrderWithUpdatedStatus() {
        // Act
        Order updatedOrder = order.withUpdatedStatus("COMPLETED");

        // Assert
        assertNotSame(order, updatedOrder);
        assertEquals("COMPLETED", updatedOrder.getOrderStatus());
        assertEquals(order.getOrderId(), updatedOrder.getOrderId());
        assertEquals(order.getClient(), updatedOrder.getClient());
        assertEquals(order.getProducts(), updatedOrder.getProducts());
        assertEquals(order.getDeliveryAddress(), updatedOrder.getDeliveryAddress());
        assertEquals(order.getOrderDate(), updatedOrder.getOrderDate());
        assertEquals(order.getTotal(), updatedOrder.getTotal());
    }

    @Test
    void shouldCreateOrderWithUpdatedClient() {
        // Arrange
        Client newClient = Client.builder()
            .id(UUID.randomUUID())
            .name("Jane Doe")
            .email("jane@example.com")
            .phone("0987654321")
            .build();

        // Act
        Order updatedOrder = order.withUpdatedClient(newClient);

        // Assert
        assertNotSame(order, updatedOrder);
        assertEquals(newClient, updatedOrder.getClient());
        assertEquals(order.getOrderId(), updatedOrder.getOrderId());
        assertEquals(order.getOrderStatus(), updatedOrder.getOrderStatus());
        assertEquals(order.getProducts(), updatedOrder.getProducts());
        assertEquals(order.getDeliveryAddress(), updatedOrder.getDeliveryAddress());
        assertEquals(order.getOrderDate(), updatedOrder.getOrderDate());
        assertEquals(order.getTotal(), updatedOrder.getTotal());
    }

    @Test
    void shouldCreateOrderWithUpdatedDeliveryAddress() {
        // Arrange
        AddressShipping newAddress = AddressShipping.builder()
            .street("456 New St")
            .city("Bogotá")
            .state("Cundinamarca")
            .zipCode("110111")
            .country("Colombia")
            .build();

        // Act
        Order updatedOrder = order.withUpdatedDeliveryAddress(newAddress);

        // Assert
        assertNotSame(order, updatedOrder);
        assertEquals(newAddress, updatedOrder.getDeliveryAddress());
        assertEquals(order.getOrderId(), updatedOrder.getOrderId());
        assertEquals(order.getClient(), updatedOrder.getClient());
        assertEquals(order.getOrderStatus(), updatedOrder.getOrderStatus());
        assertEquals(order.getProducts(), updatedOrder.getProducts());
        assertEquals(order.getOrderDate(), updatedOrder.getOrderDate());
        assertEquals(order.getTotal(), updatedOrder.getTotal());
    }

    @Test
    void shouldCreateOrderWithAddedProduct() {
        // Arrange
        Product newProduct = Product.builder()
            .id(UUID.randomUUID())
            .name("New Product")
            .description("New Description")
            .price(new BigDecimal("149.99"))
            .stock(5)
            .build();

        // Act
        Order updatedOrder = order.withAddedProduct(newProduct, 3);

        // Assert
        assertNotSame(order, updatedOrder);
        assertEquals(2, updatedOrder.getProducts().size());
        OrderItem addedItem = updatedOrder.getProducts().get(1);
        assertEquals(newProduct, addedItem.getProduct());
        assertEquals(3, addedItem.getQuantity());
        assertEquals(order.getOrderId(), updatedOrder.getOrderId());
        assertEquals(order.getClient(), updatedOrder.getClient());
        assertEquals(order.getOrderStatus(), updatedOrder.getOrderStatus());
        assertEquals(order.getDeliveryAddress(), updatedOrder.getDeliveryAddress());
        assertEquals(order.getOrderDate(), updatedOrder.getOrderDate());
    }

    @Test
    void shouldCreateOrderWithRemovedProduct() {
        // Act
        Order updatedOrder = order.withRemovedProduct(product);

        // Assert
        assertNotSame(order, updatedOrder);
        assertTrue(updatedOrder.getProducts().isEmpty());
        assertEquals(order.getOrderId(), updatedOrder.getOrderId());
        assertEquals(order.getClient(), updatedOrder.getClient());
        assertEquals(order.getOrderStatus(), updatedOrder.getOrderStatus());
        assertEquals(order.getDeliveryAddress(), updatedOrder.getDeliveryAddress());
        assertEquals(order.getOrderDate(), updatedOrder.getOrderDate());
    }

    @Test
    void shouldCalculateTotalFromProducts() {
        // Arrange
        Product product1 = Product.builder()
            .id(UUID.randomUUID())
            .name("Product 1")
            .description("Description 1")
            .price(new BigDecimal("100.00"))
            .stock(10)
            .build();

        Product product2 = Product.builder()
            .id(UUID.randomUUID())
            .name("Product 2")
            .description("Description 2")
            .price(new BigDecimal("200.00"))
            .stock(20)
            .build();

        OrderItem item1 = OrderItem.builder()
            .product(product1)
            .quantity(2)
            .build();

        OrderItem item2 = OrderItem.builder()
            .product(product2)
            .quantity(1)
            .build();

        List<OrderItem> items = new ArrayList<>(Arrays.asList(item1, item2));

        // Act
        Order orderWithItems = Order.builder()
            .orderId(UUID.randomUUID())
            .client(client)
            .orderStatus("PENDING")
            .orderDate(LocalDateTime.now())
            .deliveryAddress(deliveryAddress)
            .products(items)
            .build();

        // Assert
        OrderTotalValue total = orderWithItems.getTotal();
        assertEquals(new BigDecimal("400.00"), total.getAmount());
        assertEquals("COP", total.getCurrency());
    }

    @Test
    void shouldReturnZeroTotalWhenNoProducts() {
        // Act
        Order orderWithoutProducts = Order.builder()
            .orderId(orderId)
            .client(client)
            .orderStatus("PENDING")
            .orderDate(LocalDateTime.now())
            .deliveryAddress(deliveryAddress)
            .products(new ArrayList<>())
            .build();

        // Assert
        OrderTotalValue total = orderWithoutProducts.getTotal();
        assertEquals(BigDecimal.ZERO, total.getAmount());
        assertEquals("COP", total.getCurrency());
    }

    @Test
    void testCalculateTotalWithSingleProduct() {
        // Arrange
        Product product = Product.builder()
            .id(UUID.randomUUID())
            .name("Test Product")
            .description("Test Description")
            .price(new BigDecimal("10.99"))
            .stock(100)
            .build();

        Order emptyOrder = Order.builder()
            .orderId(UUID.randomUUID())
            .client(client)
            .orderStatus("PENDING")
            .orderDate(LocalDateTime.now())
            .deliveryAddress(deliveryAddress)
            .products(new ArrayList<>())
            .build();

        Order updatedOrder = emptyOrder.withAddedProduct(product, 2);

        // Act
        OrderTotalValue total = updatedOrder.getTotal();

        // Assert
        assertNotNull(total);
        assertEquals(new BigDecimal("21.98"), total.getAmount());
        assertEquals("COP", total.getCurrency());
    }

    @Test
    void testCalculateTotalWithMultipleProducts() {
        // Arrange
        Product product1 = Product.builder()
            .id(UUID.randomUUID())
            .name("Product 1")
            .description("Description 1")
            .price(new BigDecimal("10.99"))
            .stock(100)
            .build();

        Product product2 = Product.builder()
            .id(UUID.randomUUID())
            .name("Product 2")
            .description("Description 2")
            .price(new BigDecimal("20.99"))
            .stock(200)
            .build();

        Order emptyOrder = Order.builder()
            .orderId(UUID.randomUUID())
            .client(client)
            .orderStatus("PENDING")
            .orderDate(LocalDateTime.now())
            .deliveryAddress(deliveryAddress)
            .products(new ArrayList<>())
            .build();

        Order updatedOrder = emptyOrder.withAddedProduct(product1, 2);
        updatedOrder = updatedOrder.withAddedProduct(product2, 3);

        // Act
        OrderTotalValue total = updatedOrder.getTotal();

        // Assert
        assertNotNull(total);
        assertEquals(new BigDecimal("84.95"), total.getAmount());
        assertEquals("COP", total.getCurrency());
    }

    @Test
    void testCalculateTotalAfterRemovingProduct() {
        // Arrange
        Product product1 = Product.builder()
            .id(UUID.randomUUID())
            .name("Product 1")
            .description("Description 1")
            .price(new BigDecimal("10.99"))
            .stock(100)
            .build();

        Product product2 = Product.builder()
            .id(UUID.randomUUID())
            .name("Product 2")
            .description("Description 2")
            .price(new BigDecimal("20.99"))
            .stock(200)
            .build();

        Order emptyOrder = Order.builder()
            .orderId(UUID.randomUUID())
            .client(client)
            .orderStatus("PENDING")
            .orderDate(LocalDateTime.now())
            .deliveryAddress(deliveryAddress)
            .products(new ArrayList<>())
            .build();

        Order updatedOrder = emptyOrder.withAddedProduct(product1, 2);
        updatedOrder = updatedOrder.withAddedProduct(product2, 3);
        updatedOrder = updatedOrder.withRemovedProduct(product1);

        // Act
        OrderTotalValue total = updatedOrder.getTotal();

        // Assert
        assertNotNull(total);
        assertEquals(new BigDecimal("62.97"), total.getAmount());
        assertEquals("COP", total.getCurrency());
    }
} 