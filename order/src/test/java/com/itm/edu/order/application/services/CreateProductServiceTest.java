package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.exception.BusinessException;
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
class CreateProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private CreateProductService createProductService;

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("10.99"))
                .stock(100)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product result = createProductService.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getStock(), result.getStock());
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenProductIsNull() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createProductService.createProduct(null));
        assertEquals("El producto no puede ser nulo", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product invalidProduct = Product.createForTesting(
            productId, "", "Test Description", new BigDecimal("10.99"), 100);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createProductService.createProduct(invalidProduct));
        assertEquals("El nombre del producto no puede estar vacío", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product invalidProduct = Product.createForTesting(
            productId, "Test Product", "", new BigDecimal("10.99"), 100);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createProductService.createProduct(invalidProduct));
        assertEquals("La descripción del producto no puede estar vacía", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product invalidProduct = Product.createForTesting(
            productId, "Test Product", "Test Description", new BigDecimal("-10.99"), 100);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createProductService.createProduct(invalidProduct));
        assertEquals("El precio no puede ser negativo", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product invalidProduct = Product.createForTesting(
            productId, "Test Product", "Test Description", new BigDecimal("10.99"), -1);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createProductService.createProduct(invalidProduct));
        assertEquals("El stock no puede ser negativo", exception.getMessage());
        verify(productRepository, never()).save(any());
    }
} 