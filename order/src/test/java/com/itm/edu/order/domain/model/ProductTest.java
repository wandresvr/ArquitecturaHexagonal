package com.itm.edu.order.domain.model;

import com.itm.edu.order.domain.exception.ProductValidationException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWithValidData() {
        Product product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals(100, product.getStock());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        Product product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("-99.99"))
                .stock(100)
                .build();

        Exception exception = assertThrows(ProductValidationException.class, () -> {
            product.validate();
        });

        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStockIsNegative() {
        Product product = Product.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(-100)
                .build();

        Exception exception = assertThrows(ProductValidationException.class, () -> {
            product.validate();
        });

        assertEquals("Stock cannot be negative", exception.getMessage());
    }
} 