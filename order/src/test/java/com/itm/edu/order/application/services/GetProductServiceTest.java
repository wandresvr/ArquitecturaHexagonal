package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.domain.exception.ProductNotFoundException;
import com.itm.edu.order.domain.model.Product;
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
        Optional<Product> result = getProductService.getProduct(productId);

        // Assert
        assertTrue(result.isPresent());
        Product product = result.get();
        assertEquals(productId, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals(100, product.getStock());
        
        // Verify mock interactions
        verify(productRepositoryPort, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepositoryPort);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = getProductService.getProduct(productId);

        // Assert
        assertFalse(result.isPresent());
        
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