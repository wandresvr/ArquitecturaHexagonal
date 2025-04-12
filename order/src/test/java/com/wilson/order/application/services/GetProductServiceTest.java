package com.wilson.order.application.services;

import com.wilson.order.application.ports.outputs.ProductRepositoryPort;
import com.wilson.order.domain.exception.ProductNotFoundException;
import com.wilson.order.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private GetProductService getProductService;

    @Test
    void shouldGetProductByIdSuccessfully() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product expectedProduct = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(expectedProduct));

        // Act
        Product result = getProductService.getProduct(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(new BigDecimal("99.99"), result.getPrice());
        assertEquals(100, result.getStock());
        
        // Verify mock interactions
        verify(productRepositoryPort, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            getProductService.getProduct(productId);
        });
        
        assertEquals("Product not found", exception.getMessage());
        
        // Verify mock interactions
        verify(productRepositoryPort, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepositoryPort);
    }

    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenProductIdIsNull(UUID nullId) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            getProductService.getProduct(nullId);
        });
        
        // Verify no interactions with repository
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldGetAllProductsSuccessfully() {
        // Arrange
        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 1")
                .description("Description 1")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 2")
                .description("Description 2")
                .price(new BigDecimal("199.99"))
                .stock(200)
                .build();

        List<Product> expectedProducts = Arrays.asList(product1, product2);
        when(productRepositoryPort.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> result = getProductService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        
        // Verify mock interactions
        verify(productRepositoryPort, times(1)).findAll();
        verifyNoMoreInteractions(productRepositoryPort);
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsExist() {
        // Arrange
        when(productRepositoryPort.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Product> result = getProductService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verify mock interactions
        verify(productRepositoryPort, times(1)).findAll();
        verifyNoMoreInteractions(productRepositoryPort);
    }
} 