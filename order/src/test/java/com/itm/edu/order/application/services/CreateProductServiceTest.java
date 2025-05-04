package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private CreateProductService createProductService;

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("99.99");
        Integer stock = 100;

        Product expectedProduct = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();

        when(productRepositoryPort.save(any(Product.class))).thenReturn(expectedProduct);

        // Act
        Product result = createProductService.createProduct(name, description, price, stock);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(price, result.getPrice());
        assertEquals(stock, result.getStock());

        // Verify mock interactions
        verify(productRepositoryPort, times(1)).save(any(Product.class));
        verifyNoMoreInteractions(productRepositoryPort);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenNameIsNullOrEmpty(String invalidName) {
        // Arrange
        String description = "Test Description";
        BigDecimal price = new BigDecimal("99.99");
        Integer stock = 100;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createProductService.createProduct(invalidName, description, price, stock);
        });

        assertEquals("Product name cannot be null or empty", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(productRepositoryPort);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenDescriptionIsNullOrEmpty(String invalidDescription) {
        // Arrange
        String name = "Test Product";
        BigDecimal price = new BigDecimal("99.99");
        Integer stock = 100;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createProductService.createProduct(name, invalidDescription, price, stock);
        });

        assertEquals("Product description cannot be null or empty", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        // Arrange
        String name = "Test Product";
        String description = "Test Description";
        Integer stock = 100;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createProductService.createProduct(name, description, null, stock);
        });

        assertEquals("Product price cannot be null", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Arrange
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal negativePrice = new BigDecimal("-99.99");
        Integer stock = 100;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createProductService.createProduct(name, description, negativePrice, stock);
        });

        assertEquals("Product price cannot be negative", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenStockIsNull() {
        // Arrange
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("99.99");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createProductService.createProduct(name, description, price, null);
        });

        assertEquals("Product stock cannot be null", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        // Arrange
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("99.99");
        Integer negativeStock = -100;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createProductService.createProduct(name, description, price, negativeStock);
        });

        assertEquals("Product stock cannot be negative", exception.getMessage());

        // Verify no interactions with repository
        verifyNoInteractions(productRepositoryPort);
    }
} 