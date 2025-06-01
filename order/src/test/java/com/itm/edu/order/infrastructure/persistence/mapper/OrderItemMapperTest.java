package com.itm.edu.order.infrastructure.persistence.mapper;

import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.infrastructure.persistence.entities.OrderItemEntity;
import com.itm.edu.order.infrastructure.persistence.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemMapperTest {

    @Mock
    private ProductEntityMapper productMapper;

    @InjectMocks
    private OrderItemMapper orderItemMapper;

    private Product product;
    private ProductEntity productEntity;
    private OrderItem orderItem;
    private OrderItemEntity orderItemEntity;

    @BeforeEach
    void setUp() {
        UUID productId = UUID.randomUUID();
        
        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        productEntity = ProductEntity.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        orderItem = OrderItem.create(product, 2);

        orderItemEntity = OrderItemEntity.builder()
                .product(productEntity)
                .quantity(2)
                .build();
    }

    @Test
    void toDomain_Success() {
        // Arrange
        when(productMapper.toDomain(any(ProductEntity.class))).thenReturn(product);

        // Act
        OrderItem result = orderItemMapper.toDomain(orderItemEntity);

        // Assert
        assertNotNull(result);
        assertEquals(product, result.getProduct());
        assertEquals(2, result.getQuantity());
        verify(productMapper).toDomain(productEntity);
    }

    @Test
    void toDomain_NullEntity() {
        // Act
        OrderItem result = orderItemMapper.toDomain(null);

        // Assert
        assertNull(result);
        verifyNoInteractions(productMapper);
    }

    @Test
    void toEntity_Success() {
        // Arrange
        when(productMapper.toEntity(any(Product.class))).thenReturn(productEntity);

        // Act
        OrderItemEntity result = orderItemMapper.toEntity(orderItem);

        // Assert
        assertNotNull(result);
        assertEquals(productEntity, result.getProduct());
        assertEquals(2, result.getQuantity());
        verify(productMapper).toEntity(product);
    }

    @Test
    void toEntity_NullDomain() {
        // Act
        OrderItemEntity result = orderItemMapper.toEntity(null);

        // Assert
        assertNull(result);
        verifyNoInteractions(productMapper);
    }

    @Test
    void toEntityWithId_Success() {
        // Arrange
        when(productMapper.toEntity(any(Product.class))).thenReturn(productEntity);
        UUID existingId = UUID.randomUUID();
        OrderItemEntity existingEntity = OrderItemEntity.builder()
                .id(existingId)
                .product(productEntity)
                .quantity(2)
                .build();

        // Act
        OrderItemEntity result = orderItemMapper.toEntityWithId(orderItem, existingEntity);

        // Assert
        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals(productEntity, result.getProduct());
        assertEquals(2, result.getQuantity());
        verify(productMapper).toEntity(product);
    }

    @Test
    void toEntityWithId_NullExistingEntity() {
        // Arrange
        when(productMapper.toEntity(any(Product.class))).thenReturn(productEntity);

        // Act
        OrderItemEntity result = orderItemMapper.toEntityWithId(orderItem, null);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(productEntity, result.getProduct());
        assertEquals(2, result.getQuantity());
        verify(productMapper).toEntity(product);
    }
} 