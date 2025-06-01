package com.itm.edu.order.infrastructure.persistence.adapter;

import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.infrastructure.persistence.entities.ProductEntity;
import com.itm.edu.order.infrastructure.persistence.mapper.ProductEntityMapper;
import com.itm.edu.order.infrastructure.persistence.repository.JpaProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock
    private JpaProductRepository jpaProductRepository;

    @Mock
    private ProductEntityMapper productMapper;

    private ProductRepositoryAdapter adapter;

    private Product product;
    private ProductEntity productEntity;
    private UUID productId;

    @BeforeEach
    void setUp() {
        adapter = new ProductRepositoryAdapter(jpaProductRepository, productMapper);
        productId = UUID.randomUUID();

        product = Product.builder()
            .id(productId)
            .name("Test Product")
            .description("Test Description")
            .price(new BigDecimal("99.99"))
            .stock(10)
            .build();

        productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setName("Test Product");
        productEntity.setDescription("Test Description");
        productEntity.setPrice(new BigDecimal("99.99"));
        productEntity.setStock(10);
    }

    @Test
    void shouldSaveProductSuccessfully() {
        // Arrange
        when(productMapper.toEntity(product)).thenReturn(productEntity);
        when(jpaProductRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.toDomain(productEntity)).thenReturn(product);

        // Act
        Product savedProduct = adapter.save(product);

        // Assert
        assertNotNull(savedProduct);
        assertEquals(product, savedProduct);
        verify(productMapper).toEntity(product);
        verify(jpaProductRepository).save(productEntity);
        verify(productMapper).toDomain(productEntity);
    }

    @Test
    void shouldFindProductByIdWhenExists() {
        // Arrange
        when(jpaProductRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productMapper.toDomain(productEntity)).thenReturn(product);

        // Act
        Optional<Product> foundProduct = adapter.findById(productId);

        // Assert
        assertTrue(foundProduct.isPresent());
        assertEquals(product, foundProduct.get());
        verify(jpaProductRepository).findById(productId);
        verify(productMapper).toDomain(productEntity);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        // Arrange
        when(jpaProductRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> foundProduct = adapter.findById(productId);

        // Assert
        assertTrue(foundProduct.isEmpty());
        verify(jpaProductRepository).findById(productId);
        verify(productMapper, never()).toDomain(any());
    }

    @Test
    void shouldFindAllProducts() {
        // Arrange
        List<ProductEntity> entities = Arrays.asList(productEntity);
        List<Product> products = Arrays.asList(product);
        when(jpaProductRepository.findAll()).thenReturn(entities);
        when(productMapper.toDomain(productEntity)).thenReturn(product);

        // Act
        List<Product> foundProducts = adapter.findAll();

        // Assert
        assertNotNull(foundProducts);
        assertEquals(products, foundProducts);
        verify(jpaProductRepository).findAll();
        verify(productMapper).toDomain(productEntity);
    }

    @Test
    void shouldDeleteProductById() {
        // Act
        adapter.deleteById(productId);

        // Assert
        verify(jpaProductRepository).deleteById(productId);
    }
} 