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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private UUID productId;
    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        
        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("10.99"))
                .stock(100)
                .build();

        productDto = ProductDto.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("10.99"))
                .stock(100)
                .build();
    }

    @Test
    void createProduct_Success() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(createProductUseCase.createProduct(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.createProduct(productDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productDto, response.getBody());
        verify(productMapper).toDomain(productDto);
        verify(createProductUseCase).createProduct(product);
        verify(productMapper).toDto(product);
    }

    @Test
    void createProduct_BusinessException() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(createProductUseCase.createProduct(any(Product.class)))
            .thenThrow(new BusinessException("Error al crear el producto"));

        // Act
        ResponseEntity<?> response = productController.createProduct(productDto);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void getProduct_Success() {
        // Arrange
        when(getProductUseCase.getProduct(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.getProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void getProduct_NotFound() {
        // Arrange
        when(getProductUseCase.getProduct(productId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = productController.getProduct(productId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getProduct_NullId() {
        // Act
        ResponseEntity<?> response = productController.getProduct(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAllProducts_Success() {
        // Arrange
        List<Product> products = Arrays.asList(product);
        List<ProductDto> productDtos = Arrays.asList(productDto);
        when(getProductUseCase.getAllProducts()).thenReturn(products);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        assertEquals(1, ((List<?>) response.getBody()).size());
    }

    @Test
    void updateProduct_Success() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(updateProductUseCase.updateProduct(eq(productId), any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.updateProduct(productId, productDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void updateProduct_NullId() {
        // Act
        ResponseEntity<?> response = productController.updateProduct(null, productDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateProduct_BusinessException() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(updateProductUseCase.updateProduct(eq(productId), any(Product.class)))
            .thenThrow(new BusinessException("Error al actualizar el producto"));

        // Act
        ResponseEntity<?> response = productController.updateProduct(productId, productDto);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    void deleteProduct_Success() {
        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteProductUseCase).deleteProduct(productId);
    }

    @Test
    void deleteProduct_NullId() {
        // Act
        ResponseEntity<?> response = productController.deleteProduct(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteProduct_BusinessException() {
        // Arrange
        doThrow(new BusinessException("Error al eliminar el producto"))
            .when(deleteProductUseCase).deleteProduct(productId);

        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
} 