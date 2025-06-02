package com.itm.edu.order.infrastructure.persistence.mapper;

import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.infrastructure.persistence.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductEntityMapperTest {

    private ProductEntityMapper productEntityMapper;
    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        productEntityMapper = new ProductEntityMapper();
        
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
    }

    @Test
    void toDomain_Success() {
        // Act
        Product result = productEntityMapper.toDomain(productEntity);

        // Assert
        assertNotNull(result);
        assertEquals(productEntity.getId(), result.getId());
        assertEquals(productEntity.getName(), result.getName());
        assertEquals(productEntity.getDescription(), result.getDescription());
        assertEquals(productEntity.getPrice(), result.getPrice());
        assertEquals(productEntity.getStock(), result.getStock());
    }

    @Test
    void toDomain_NullEntity() {
        // Act
        Product result = productEntityMapper.toDomain(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_Success() {
        // Act
        ProductEntity result = productEntityMapper.toEntity(product);

        // Assert
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getStock(), result.getStock());
    }

    @Test
    void toEntity_NullDomain() {
        // Act
        ProductEntity result = productEntityMapper.toEntity(null);

        // Assert
        assertNull(result);
    }
} 