package com.itm.edu.order.infrastructure.persistence;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import com.itm.edu.order.infrastructure.persistence.entities.OrderEntity;
import com.itm.edu.order.infrastructure.persistence.mapper.OrderMapper;
import com.itm.edu.order.infrastructure.persistence.repository.JpaOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryAdapterTest {

    @Mock
    private JpaOrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderRepositoryAdapter orderRepositoryAdapter;

    private Order testOrder;
    private OrderEntity testOrderEntity;
    private UUID testId;
    private Client testClient;
    private Product testProduct;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        
        testClient = Client.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        testProduct = Product.create(
                "Test Product",
                "Test Description",
                new BigDecimal("10.99"),
                100
        );

        testOrderItem = OrderItem.builder()
                .product(testProduct)
                .quantity(2)
                .build();

        testOrder = Order.builder()
                .orderId(testId)
                .client(testClient)
                .products(Arrays.asList(testOrderItem))
                .orderStatus("PENDING")
                .orderDate(LocalDateTime.now())
                .deliveryAddress(AddressShipping.builder()
                        .street("123 Main St")
                        .city("Test City")
                        .state("Test State")
                        .zipCode("12345")
                        .country("Test Country")
                        .build())
                .total(new OrderTotalValue(new BigDecimal("21.98"), "USD"))
                .build();

        testOrderEntity = OrderEntity.builder()
                .orderId(testId)
                .orderStatus("PENDING")
                .orderDate(testOrder.getOrderDate())
                .deliveryAddress(testOrder.getDeliveryAddress())
                .total(testOrder.getTotal())
                .build();
    }

    @Test
    void save_ShouldPersistOrder() {
        // Arrange
        when(orderMapper.toEntity(testOrder)).thenReturn(testOrderEntity);
        when(orderRepository.save(testOrderEntity)).thenReturn(testOrderEntity);
        when(orderMapper.toDomain(testOrderEntity)).thenReturn(testOrder);

        // Act
        Order saved = orderRepositoryAdapter.save(testOrder);

        // Assert
        assertNotNull(saved);
        assertEquals(testOrder, saved);
        verify(orderMapper).toEntity(testOrder);
        verify(orderRepository).save(testOrderEntity);
        verify(orderMapper).toDomain(testOrderEntity);
    }

    @Test
    void findById_WhenOrderExists_ShouldReturnOrder() {
        // Arrange
        when(orderRepository.findById(testId)).thenReturn(Optional.of(testOrderEntity));
        when(orderMapper.toDomain(testOrderEntity)).thenReturn(testOrder);

        // Act
        Optional<Order> found = orderRepositoryAdapter.findById(testId);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(testOrder, found.get());
        verify(orderRepository).findById(testId);
        verify(orderMapper).toDomain(testOrderEntity);
    }

    @Test
    void findById_WhenOrderDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(orderRepository.findById(testId)).thenReturn(Optional.empty());

        // Act
        Optional<Order> found = orderRepositoryAdapter.findById(testId);

        // Assert
        assertFalse(found.isPresent());
        verify(orderRepository).findById(testId);
        verify(orderMapper, never()).toDomain(any());
    }

    @Test
    void findAll_ShouldReturnAllOrders() {
        // Arrange
        List<OrderEntity> entities = Arrays.asList(testOrderEntity);
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(entities);
        when(orderMapper.toDomain(testOrderEntity)).thenReturn(testOrder);

        // Act
        List<Order> found = orderRepositoryAdapter.findAll();

        // Assert
        assertNotNull(found);
        assertEquals(orders, found);
        verify(orderRepository).findAll();
        verify(orderMapper).toDomain(testOrderEntity);
    }

    @Test
    void delete_ShouldRemoveOrder() {
        // Arrange
        doNothing().when(orderRepository).deleteById(testId);

        // Act
        orderRepositoryAdapter.deleteById(testId);

        // Assert
        verify(orderRepository).deleteById(testId);
    }
} 