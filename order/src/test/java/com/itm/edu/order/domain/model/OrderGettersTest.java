package com.itm.edu.order.domain.model;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderGettersTest {

    private UUID orderId;
    private Client client;
    private String orderStatus;
    private LocalDateTime orderDate;
    private AddressShipping deliveryAddress;
    private OrderTotalValue total;
    private List<OrderItem> products;
    private Order order;

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

        orderStatus = "PENDING";
        orderDate = LocalDateTime.now();

        deliveryAddress = AddressShipping.builder()
            .street("123 Main St")
            .city("Medellín")
            .state("Antioquia")
            .zipCode("050001")
            .country("Colombia")
            .build();

        Product product = Product.builder()
            .id(productId)
            .name("Test Product")
            .description("Test Description")
            .price(new BigDecimal("99.99"))
            .stock(10)
            .build();

        OrderItem orderItem = OrderItem.builder()
            .product(product)
            .quantity(2)
            .build();

        products = new ArrayList<>(Collections.singletonList(orderItem));
        total = new OrderTotalValue(new BigDecimal("199.98"), "COP");

        order = Order.builder()
            .orderId(orderId)
            .client(client)
            .orderStatus(orderStatus)
            .orderDate(orderDate)
            .deliveryAddress(deliveryAddress)
            .total(total)
            .products(products)
            .build();
    }

    @Test
    @DisplayName("Debería obtener el ID de la orden correctamente")
    void getOrderId_ShouldReturnCorrectOrderId() {
        // Act
        UUID result = order.getOrderId();

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result);
    }

    @Test
    @DisplayName("Debería obtener el cliente correctamente")
    void getClient_ShouldReturnCorrectClient() {
        // Act
        Client result = order.getClient();

        // Assert
        assertNotNull(result);
        assertEquals(client.getId(), result.getId());
        assertEquals(client.getName(), result.getName());
        assertEquals(client.getEmail(), result.getEmail());
        assertEquals(client.getPhone(), result.getPhone());
    }

    @Test
    @DisplayName("Debería obtener el estado de la orden correctamente")
    void getOrderStatus_ShouldReturnCorrectOrderStatus() {
        // Act
        String result = order.getOrderStatus();

        // Assert
        assertNotNull(result);
        assertEquals(orderStatus, result);
    }

    @Test
    @DisplayName("Debería obtener la fecha de la orden correctamente")
    void getOrderDate_ShouldReturnCorrectOrderDate() {
        // Act
        LocalDateTime result = order.getOrderDate();

        // Assert
        assertNotNull(result);
        assertEquals(orderDate, result);
    }

    @Test
    @DisplayName("Debería obtener la dirección de entrega correctamente")
    void getDeliveryAddress_ShouldReturnCorrectDeliveryAddress() {
        // Act
        AddressShipping result = order.getDeliveryAddress();

        // Assert
        assertNotNull(result);
        assertEquals(deliveryAddress.getStreet(), result.getStreet());
        assertEquals(deliveryAddress.getCity(), result.getCity());
        assertEquals(deliveryAddress.getState(), result.getState());
        assertEquals(deliveryAddress.getZipCode(), result.getZipCode());
        assertEquals(deliveryAddress.getCountry(), result.getCountry());
    }

    @Test
    @DisplayName("Debería obtener el total correctamente")
    void getTotal_ShouldReturnCorrectTotal() {
        // Act
        OrderTotalValue result = order.getTotal();

        // Assert
        assertNotNull(result);
        assertEquals(total.getAmount(), result.getAmount());
        assertEquals(total.getCurrency(), result.getCurrency());
    }

    @Test
    @DisplayName("Debería obtener los productos correctamente")
    void getProducts_ShouldReturnCorrectProducts() {
        // Act
        List<OrderItem> result = order.getProducts();

        // Assert
        assertNotNull(result);
        assertEquals(products.size(), result.size());
        assertEquals(products.get(0).getProduct().getId(), result.get(0).getProduct().getId());
        assertEquals(products.get(0).getQuantity(), result.get(0).getQuantity());
    }

    @Test
    @DisplayName("Debería obtener una lista vacía cuando no hay productos")
    void getProducts_WhenEmpty_ShouldReturnEmptyList() {
        // Arrange
        Order emptyOrder = Order.builder()
            .orderId(orderId)
            .client(client)
            .orderStatus(orderStatus)
            .orderDate(orderDate)
            .deliveryAddress(deliveryAddress)
            .total(total)
            .products(new ArrayList<>())
            .build();

        // Act
        List<OrderItem> result = emptyOrder.getProducts();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Debería mantener la inmutabilidad de los campos finales")
    void shouldMaintainImmutabilityOfFinalFields() {
        // Arrange
        UUID newOrderId = UUID.randomUUID();
        Client newClient = Client.builder()
            .id(UUID.randomUUID())
            .name("Jane Doe")
            .email("jane@example.com")
            .phone("0987654321")
            .build();
        LocalDateTime newOrderDate = LocalDateTime.now().plusDays(1);
        AddressShipping newAddress = AddressShipping.builder()
            .street("456 New St")
            .city("Bogotá")
            .state("Cundinamarca")
            .zipCode("110001")
            .country("Colombia")
            .build();

        // Act & Assert
        assertNotEquals(newOrderId, order.getOrderId());
        assertNotEquals(newClient.getId(), order.getClient().getId());
        assertNotEquals(newOrderDate, order.getOrderDate());
        assertNotEquals(newAddress.getStreet(), order.getDeliveryAddress().getStreet());
    }

    @Test
    @DisplayName("Debería obtener la misma referencia de la lista de productos")
    void getProducts_ShouldReturnSameListReference() {
        // Act
        List<OrderItem> firstCall = order.getProducts();
        List<OrderItem> secondCall = order.getProducts();

        // Assert
        assertSame(firstCall, secondCall, "El getter debería devolver la misma referencia de la lista");
        assertSame(order.getProducts(), firstCall, "El getter debería devolver la referencia directa a la lista interna");
    }

    @Test
    @DisplayName("Debería obtener la misma referencia del cliente")
    void getClient_ShouldReturnSameClientReference() {
        // Act
        Client firstCall = order.getClient();
        Client secondCall = order.getClient();

        // Assert
        assertSame(firstCall, secondCall, "El getter debería devolver la misma referencia del cliente");
    }

    @Test
    @DisplayName("Debería obtener la misma referencia de la dirección de entrega")
    void getDeliveryAddress_ShouldReturnSameAddressReference() {
        // Act
        AddressShipping firstCall = order.getDeliveryAddress();
        AddressShipping secondCall = order.getDeliveryAddress();

        // Assert
        assertSame(firstCall, secondCall, "El getter debería devolver la misma referencia de la dirección");
    }

    @Test
    @DisplayName("Debería obtener la misma referencia del total")
    void getTotal_ShouldReturnSameTotalReference() {
        // Act
        OrderTotalValue firstCall = order.getTotal();
        OrderTotalValue secondCall = order.getTotal();

        // Assert
        assertSame(firstCall, secondCall, "El getter debería devolver la misma referencia del total");
    }

    @Test
    @DisplayName("Debería obtener la misma referencia de la fecha de la orden")
    void getOrderDate_ShouldReturnSameDateReference() {
        // Act
        LocalDateTime firstCall = order.getOrderDate();
        LocalDateTime secondCall = order.getOrderDate();

        // Assert
        assertSame(firstCall, secondCall, "El getter debería devolver la misma referencia de la fecha");
    }

    @Test
    @DisplayName("Debería obtener la misma referencia del ID de la orden")
    void getOrderId_ShouldReturnSameIdReference() {
        // Act
        UUID firstCall = order.getOrderId();
        UUID secondCall = order.getOrderId();

        // Assert
        assertSame(firstCall, secondCall, "El getter debería devolver la misma referencia del ID");
    }

    @Test
    @DisplayName("Debería obtener la misma referencia del estado de la orden")
    void getOrderStatus_ShouldReturnSameStatusReference() {
        // Act
        String firstCall = order.getOrderStatus();
        String secondCall = order.getOrderStatus();

        // Assert
        assertSame(firstCall, secondCall, "El getter debería devolver la misma referencia del estado");
    }

    @Test
    @DisplayName("Debería mantener la inmutabilidad de la lista de productos")
    void shouldMaintainImmutabilityOfProductsList() {
        // Arrange
        List<OrderItem> originalProducts = new ArrayList<>(order.getProducts());
        OrderItem newItem = OrderItem.builder()
            .product(Product.builder()
                .id(UUID.randomUUID())
                .name("New Product")
                .description("New Description")
                .price(new BigDecimal("149.99"))
                .stock(5)
                .build())
            .quantity(1)
            .build();

        // Act
        List<OrderItem> result = order.getProducts();
        result.add(newItem);

        // Assert
        assertEquals(originalProducts.size() + 1, result.size());
        assertEquals(originalProducts.size() + 1, order.getProducts().size());
        assertTrue(order.getProducts().containsAll(originalProducts));
        assertTrue(order.getProducts().contains(newItem));
    }

    @Test
    @DisplayName("Debería permitir agregar productos a través del método addProduct")
    void shouldAllowAddingProductsThroughAddProductMethod() {
        // Arrange
        List<OrderItem> originalProducts = new ArrayList<>(order.getProducts());
        Product newProduct = Product.builder()
            .id(UUID.randomUUID())
            .name("New Product")
            .description("New Description")
            .price(new BigDecimal("149.99"))
            .stock(5)
            .build();

        // Act
        order.addProduct(newProduct, 1);

        // Assert
        assertEquals(originalProducts.size() + 1, order.getProducts().size());
        assertTrue(order.getProducts().containsAll(originalProducts));
        assertTrue(order.getProducts().stream()
            .anyMatch(item -> item.getProduct().getId().equals(newProduct.getId())));
    }

    @Test
    @DisplayName("Debería mantener la inmutabilidad de la lista de productos al usar withAddedProduct")
    void shouldMaintainImmutabilityOfProductsListWhenUsingWithAddedProduct() {
        // Arrange
        List<OrderItem> originalProducts = new ArrayList<>(order.getProducts());
        Product newProduct = Product.builder()
            .id(UUID.randomUUID())
            .name("New Product")
            .description("New Description")
            .price(new BigDecimal("149.99"))
            .stock(5)
            .build();

        // Act
        Order newOrder = order.withAddedProduct(newProduct, 1);

        // Assert
        assertEquals(originalProducts.size(), order.getProducts().size());
        assertEquals(originalProducts.size() + 1, newOrder.getProducts().size());
        assertTrue(newOrder.getProducts().containsAll(originalProducts));
        assertTrue(newOrder.getProducts().stream()
            .anyMatch(item -> item.getProduct().getId().equals(newProduct.getId())));
    }

    @Test
    @DisplayName("Debería mantener la inmutabilidad del valor total")
    void shouldMaintainImmutabilityOfTotal() {
        // Arrange
        OrderTotalValue originalTotal = order.getTotal();
        BigDecimal newAmount = new BigDecimal("299.99");

        // Act
        OrderTotalValue result = order.getTotal();
        result.addAmount(new BigDecimal("100.00"));

        // Assert
        assertEquals(originalTotal.getAmount(), order.getTotal().getAmount());
        assertNotEquals(newAmount, order.getTotal().getAmount());
    }

    @Test
    @DisplayName("Debería obtener valores correctos de los getters después de actualizar el estado")
    void getters_ShouldReturnCorrectValuesAfterStatusUpdate() {
        // Arrange
        String newStatus = "PROCESSING";
        
        // Act
        order.updateStatus(newStatus);
        
        // Assert
        assertEquals(newStatus, order.getOrderStatus());
        assertEquals(orderId, order.getOrderId());
        assertEquals(client, order.getClient());
        assertEquals(orderDate, order.getOrderDate());
        assertEquals(deliveryAddress, order.getDeliveryAddress());
        assertEquals(total, order.getTotal());
        assertEquals(products, order.getProducts());
    }

    @Test
    @DisplayName("Debería obtener valores correctos de los getters después de crear una nueva orden")
    void getters_ShouldReturnCorrectValuesAfterCreatingNewOrder() {
        // Arrange
        Order newOrder = Order.create(client, products, deliveryAddress);
        
        // Assert
        assertNotNull(newOrder.getOrderId());
        assertEquals(client, newOrder.getClient());
        assertEquals("PENDING_VALIDATION", newOrder.getOrderStatus());
        assertNotNull(newOrder.getOrderDate());
        assertEquals(deliveryAddress, newOrder.getDeliveryAddress());
        assertEquals(products, newOrder.getProducts());
        assertNotNull(newOrder.getTotal());
    }
} 