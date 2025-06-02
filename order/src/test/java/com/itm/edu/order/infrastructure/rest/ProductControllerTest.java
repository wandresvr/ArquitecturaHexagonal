package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.CreateProductUseCase;
import com.itm.edu.order.application.ports.inputs.GetProductUseCase;
import com.itm.edu.order.application.ports.inputs.UpdateProductUseCase;
import com.itm.edu.order.application.ports.inputs.DeleteProductUseCase;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.infrastructure.rest.dto.ProductDto;
import com.itm.edu.order.infrastructure.rest.mapper.ProductDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private CreateProductUseCase createProductUseCase;

    @Mock
    private GetProductUseCase getProductUseCase;

    @Mock
    private UpdateProductUseCase updateProductUseCase;

    @Mock
    private DeleteProductUseCase deleteProductUseCase;

    @Mock
    private ProductDtoMapper productMapper;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProductSuccess() {
        // Arrange
        ProductDto productDto = ProductDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .build();

        when(productMapper.toDomain(productDto)).thenReturn(product);
        when(createProductUseCase.createProduct(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.createProduct(productDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(createProductUseCase).createProduct(product);
        verify(productMapper).toDomain(productDto);
        verify(productMapper).toDto(product);
    }

    @Test
    void testGetProductByIdSuccess() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product product = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        ProductDto productDto = ProductDto.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        when(getProductUseCase.getProduct(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.getProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(getProductUseCase).getProduct(productId);
        verify(productMapper).toDto(product);
    }

    @Test
    void testGetProductByIdNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();
        when(getProductUseCase.getProduct(productId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = productController.getProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(getProductUseCase).getProduct(productId);
        verifyNoInteractions(productMapper);
    }

    @Test
    void testGetAllProductsSuccess() {
        // Arrange
        List<Product> products = Arrays.asList(
                Product.builder()
                        .id(UUID.randomUUID())
                        .name("Product 1")
                        .description("Description 1")
                        .price(new BigDecimal("99.99"))
                        .stock(100)
                        .build(),
                Product.builder()
                        .id(UUID.randomUUID())
                        .name("Product 2")
                        .description("Description 2")
                        .price(new BigDecimal("149.99"))
                        .stock(200)
                        .build()
        );

        List<ProductDto> productDtos = Arrays.asList(
                ProductDto.builder()
                        .id(products.get(0).getId())
                        .name("Product 1")
                        .description("Description 1")
                        .price(new BigDecimal("99.99"))
                        .stock(100)
                        .build(),
                ProductDto.builder()
                        .id(products.get(1).getId())
                        .name("Product 2")
                        .description("Description 2")
                        .price(new BigDecimal("149.99"))
                        .stock(200)
                        .build()
        );

        when(getProductUseCase.getAllProducts()).thenReturn(products);
        when(productMapper.toDto(products.get(0))).thenReturn(productDtos.get(0));
        when(productMapper.toDto(products.get(1))).thenReturn(productDtos.get(1));

        // Act
        ResponseEntity<?> response = productController.getAllProducts();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDtos, response.getBody());
        verify(getProductUseCase).getAllProducts();
        verify(productMapper).toDto(products.get(0));
        verify(productMapper).toDto(products.get(1));
    }

    @Test
    void testGetAllProductsEmpty() {
        // Arrange
        when(getProductUseCase.getAllProducts()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<?> response = productController.getAllProducts();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(getProductUseCase).getAllProducts();
        verifyNoInteractions(productMapper);
    }

    @Test
    void testUpdateProductSuccess() {
        // Arrange
        UUID productId = UUID.randomUUID();
        ProductDto productDto = ProductDto.builder()
                .name("Updated Product")
                .description("Updated Description")
                .price(new BigDecimal("199.99"))
                .stock(150)
                .build();

        Product product = Product.builder()
                .id(productId)
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .build();

        when(productMapper.toDomain(productDto)).thenReturn(product);
        when(updateProductUseCase.updateProduct(eq(productId), any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.updateProduct(productId, productDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(updateProductUseCase).updateProduct(eq(productId), any(Product.class));
        verify(productMapper).toDomain(productDto);
        verify(productMapper).toDto(product);
    }

    @Test
    void testDeleteProductSuccess() {
        // Arrange
        UUID productId = UUID.randomUUID();
        doNothing().when(deleteProductUseCase).deleteProduct(productId);

        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteProductUseCase).deleteProduct(productId);
    }

    @Test
    void testGetProductWithNullId() {
        // Act
        ResponseEntity<?> response = productController.getProduct(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(getProductUseCase);
    }

    @Test
    void testUpdateProductWithNullId() {
        // Arrange
        ProductDto productDto = ProductDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .stock(100)
                .build();

        // Act
        ResponseEntity<?> response = productController.updateProduct(null, productDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(updateProductUseCase);
    }

    @Test
    void testDeleteProductWithNullId() {
        // Act
        ResponseEntity<?> response = productController.deleteProduct(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(deleteProductUseCase);
    }
} 