package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.domain.exception.ProductNotFoundException;
import com.itm.edu.order.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private DeleteProductService deleteProductService;

    @Test
    void shouldDeleteProductSuccessfully() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product existingProduct = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(java.math.BigDecimal.TEN)
                .stock(100)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).deleteById(productId);

        // Act
        assertDoesNotThrow(() -> deleteProductService.deleteProduct(productId));

        // Assert
        verify(productRepository).findById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> deleteProductService.deleteProduct(null));
        assertEquals("Product ID cannot be null", exception.getMessage());
        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Arrange
        UUID nonExistentProductId = UUID.randomUUID();
        when(productRepository.findById(nonExistentProductId)).thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> deleteProductService.deleteProduct(nonExistentProductId));
        assertEquals("Product not found with id: " + nonExistentProductId, exception.getMessage());
        verify(productRepository).findById(nonExistentProductId);
        verify(productRepository, never()).deleteById(any());
    }
} 