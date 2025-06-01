package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.*;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.infrastructure.rest.dto.ProductDto;
import com.itm.edu.order.infrastructure.rest.mapper.ProductDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerExceptionTest {

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

    private ProductDto productDto;
    private Product product;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        productDto = ProductDto.builder()
            .name("Test Product")
            .description("Test Description")
            .price(new BigDecimal("10.99"))
            .stock(100)
            .build();

        product = Product.create(
            productDto.getName(),
            productDto.getDescription(),
            productDto.getPrice(),
            productDto.getStock()
        );
    }

    @Test
    void createProduct_WhenBusinessException_ShouldReturnUnprocessableEntity() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(createProductUseCase.createProduct(any(Product.class)))
            .thenThrow(new BusinessException("Error de negocio"));

        // Act
        ResponseEntity<?> response = productController.createProduct(productDto);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createProduct_WhenUnexpectedException_ShouldReturnInternalServerError() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(createProductUseCase.createProduct(any(Product.class)))
            .thenThrow(new RuntimeException("Error inesperado"));

        // Act
        ResponseEntity<?> response = productController.createProduct(productDto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getProduct_WhenIdIsNull_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<?> response = productController.getProduct(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getProduct_WhenUnexpectedException_ShouldReturnInternalServerError() {
        // Arrange
        when(getProductUseCase.getProduct(any(UUID.class)))
            .thenThrow(new RuntimeException("Error inesperado"));

        // Act
        ResponseEntity<?> response = productController.getProduct(productId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllProducts_WhenUnexpectedException_ShouldReturnInternalServerError() {
        // Arrange
        when(getProductUseCase.getAllProducts())
            .thenThrow(new RuntimeException("Error inesperado"));

        // Act
        ResponseEntity<?> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateProduct_WhenIdIsNull_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<?> response = productController.updateProduct(null, productDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateProduct_WhenBusinessException_ShouldReturnUnprocessableEntity() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(updateProductUseCase.updateProduct(any(UUID.class), any(Product.class)))
            .thenThrow(new BusinessException("Error de negocio"));

        // Act
        ResponseEntity<?> response = productController.updateProduct(productId, productDto);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateProduct_WhenUnexpectedException_ShouldReturnInternalServerError() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(updateProductUseCase.updateProduct(any(UUID.class), any(Product.class)))
            .thenThrow(new RuntimeException("Error inesperado"));

        // Act
        ResponseEntity<?> response = productController.updateProduct(productId, productDto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteProduct_WhenUnexpectedException_ShouldReturnInternalServerError() {
        // Arrange
        doThrow(new RuntimeException("Error inesperado"))
            .when(deleteProductUseCase).deleteProduct(any(UUID.class));

        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }
} 