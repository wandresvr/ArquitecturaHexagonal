package com.wilson.order.infrastructure.rest;

import com.wilson.order.application.ports.inputs.CreateProductUseCase;
import com.wilson.order.application.ports.inputs.GetProductUseCase;
import com.wilson.order.domain.model.Product;
import com.wilson.order.infrastructure.rest.dto.CreateProductRequestDto;
import com.wilson.order.infrastructure.rest.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    @Mock
    private CreateProductUseCase createProductUseCase;

    @Mock
    private GetProductUseCase getProductUseCase;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProductSuccess() {
        // Arrange
        CreateProductRequestDto request = CreateProductRequestDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        Product expectedProduct = Product.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        when(createProductUseCase.createProduct(any(), any(), any(), any()))
                .thenReturn(expectedProduct);

        // Act
        ResponseEntity<ProductDto> response = productController.createProduct(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(expectedProduct.getId(), response.getBody().getId());
        assertEquals(expectedProduct.getName(), response.getBody().getName());
        assertEquals(expectedProduct.getDescription(), response.getBody().getDescription());
        assertEquals(expectedProduct.getPrice(), response.getBody().getPrice());
        assertEquals(expectedProduct.getStock(), response.getBody().getStock());
    }

    @Test
    void testGetProductSuccess() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product expectedProduct = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        when(getProductUseCase.getProduct(productId))
                .thenReturn(expectedProduct);

        // Act
        ResponseEntity<ProductDto> response = productController.getProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(expectedProduct.getId(), response.getBody().getId());
        assertEquals(expectedProduct.getName(), response.getBody().getName());
        assertEquals(expectedProduct.getDescription(), response.getBody().getDescription());
        assertEquals(expectedProduct.getPrice(), response.getBody().getPrice());
        assertEquals(expectedProduct.getStock(), response.getBody().getStock());
    }

    @Test
    void testGetAllProductsSuccess() {
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
                .stock(50)
                .build();

        List<Product> expectedProducts = Arrays.asList(product1, product2);

        when(getProductUseCase.getAllProducts())
                .thenReturn(expectedProducts);

        // Act
        ResponseEntity<List<ProductDto>> response = productController.getAllProducts();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        ProductDto dto1 = response.getBody().get(0);
        assertEquals(product1.getId(), dto1.getId());
        assertEquals(product1.getName(), dto1.getName());
        assertEquals(product1.getDescription(), dto1.getDescription());
        assertEquals(product1.getPrice(), dto1.getPrice());
        assertEquals(product1.getStock(), dto1.getStock());

        ProductDto dto2 = response.getBody().get(1);
        assertEquals(product2.getId(), dto2.getId());
        assertEquals(product2.getName(), dto2.getName());
        assertEquals(product2.getDescription(), dto2.getDescription());
        assertEquals(product2.getPrice(), dto2.getPrice());
        assertEquals(product2.getStock(), dto2.getStock());
    }
} 