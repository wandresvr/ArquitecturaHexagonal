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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private UpdateProductService updateProductService;

    @Test
    void shouldUpdateProductSuccessfully() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.builder()
                .id(productId)
                .name("Old Name")
                .description("Old Description")
                .price(new BigDecimal("9.99"))
                .stock(50)
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name("New Name")
                .description("New Description")
                .price(new BigDecimal("19.99"))
                .stock(100)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        Product result = updateProductService.updateProduct(productId, updatedProduct);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("New Name", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(new BigDecimal("19.99"), result.getPrice());
        assertEquals(100, result.getStock());
        verify(productRepository).findById(productId);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product updatedProduct = Product.builder()
                .id(productId)
                .name("New Name")
                .description("New Description")
                .price(new BigDecimal("19.99"))
                .stock(100)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> updateProductService.updateProduct(productId, updatedProduct));
        assertEquals("Producto no encontrado con ID: " + productId, exception.getMessage());
        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdateProductIsNull() {
        // Arrange
        UUID productId = UUID.randomUUID();

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> updateProductService.updateProduct(productId, null));
        assertEquals("El producto no puede ser nulo", exception.getMessage());
        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.builder()
                .id(productId)
                .name("Old Name")
                .description("Old Description")
                .price(new BigDecimal("9.99"))
                .stock(50)
                .build();

        Product invalidProduct = Product.createForTesting(
            productId, "", "New Description", new BigDecimal("19.99"), 100);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> updateProductService.updateProduct(productId, invalidProduct));
        assertEquals("El nombre del producto no puede estar vacío", exception.getMessage());
        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.builder()
                .id(productId)
                .name("Old Name")
                .description("Old Description")
                .price(new BigDecimal("9.99"))
                .stock(50)
                .build();

        Product invalidProduct = Product.createForTesting(
            productId, "New Name", "", new BigDecimal("19.99"), 100);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> updateProductService.updateProduct(productId, invalidProduct));
        assertEquals("La descripción del producto no puede estar vacía", exception.getMessage());
        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.builder()
                .id(productId)
                .name("Old Name")
                .description("Old Description")
                .price(new BigDecimal("9.99"))
                .stock(50)
                .build();

        Product invalidProduct = Product.createForTesting(
            productId, "New Name", "New Description", new BigDecimal("-19.99"), 100);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> updateProductService.updateProduct(productId, invalidProduct));
        assertEquals("El precio no puede ser negativo", exception.getMessage());
        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.builder()
                .id(productId)
                .name("Old Name")
                .description("Old Description")
                .price(new BigDecimal("9.99"))
                .stock(50)
                .build();

        Product invalidProduct = Product.createForTesting(
            productId, "New Name", "New Description", new BigDecimal("19.99"), -1);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> updateProductService.updateProduct(productId, invalidProduct));
        assertEquals("El stock no puede ser negativo", exception.getMessage());
        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
    }
} 