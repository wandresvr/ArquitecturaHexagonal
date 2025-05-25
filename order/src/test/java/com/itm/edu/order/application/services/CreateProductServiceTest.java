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
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private CreateProductService createProductService;

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        Product product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        when(productRepositoryPort.save(any(Product.class))).thenReturn(product);

        // Act
        Product result = createProductService.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getStock(), result.getStock());
        verify(productRepositoryPort).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductIsNull() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createProductService.createProduct(null);
        });

        assertEquals("El producto no puede ser nulo", exception.getMessage());
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .name("")
                    .description("Test Description")
                    .price(new BigDecimal("99.99"))
                    .stock(100)
                    .build();
        });

        assertEquals("El nombre del producto no puede estar vacío", exception.getMessage());
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .name("Test Product")
                    .description("")
                    .price(new BigDecimal("99.99"))
                    .stock(100)
                    .build();
        });

        assertEquals("La descripción del producto no puede estar vacía", exception.getMessage());
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .name("Test Product")
                    .description("Test Description")
                    .price(null)
                    .stock(100)
                    .build();
        });

        assertEquals("El precio no puede ser negativo", exception.getMessage());
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .name("Test Product")
                    .description("Test Description")
                    .price(new BigDecimal("-1.00"))
                    .stock(100)
                    .build();
        });

        assertEquals("El precio no puede ser negativo", exception.getMessage());
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenStockIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .name("Test Product")
                    .description("Test Description")
                    .price(new BigDecimal("99.99"))
                    .stock(null)
                    .build();
        });

        assertEquals("El stock no puede ser negativo", exception.getMessage());
        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Product.builder()
                    .name("Test Product")
                    .description("Test Description")
                    .price(new BigDecimal("99.99"))
                    .stock(-1)
                    .build();
        });

        assertEquals("El stock no puede ser negativo", exception.getMessage());
        verifyNoInteractions(productRepositoryPort);
    }
} 