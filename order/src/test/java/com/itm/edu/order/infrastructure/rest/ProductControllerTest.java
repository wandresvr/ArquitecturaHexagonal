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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    void createProduct_ShouldReturnCreatedProduct() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(createProductUseCase.createProduct(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.createProduct(productDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(productDto, response.getBody());
        verify(createProductUseCase).createProduct(any(Product.class));
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
    }

    @Test
    void getProduct_WhenProductExists_ShouldReturnProduct() {
        // Arrange
        when(getProductUseCase.getProduct(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.getProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void getProduct_WhenProductDoesNotExist_ShouldReturnNotFound() {
        // Arrange
        when(getProductUseCase.getProduct(productId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = productController.getProduct(productId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getProduct_WhenIdIsNull_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<?> response = productController.getProduct(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        // Arrange
        List<Product> products = List.of(product);
        List<ProductDto> productDtos = List.of(productDto);
        when(getProductUseCase.getAllProducts()).thenReturn(products);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDtos, response.getBody());
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() {
        // Arrange
        when(productMapper.toDomain(any(ProductDto.class))).thenReturn(product);
        when(updateProductUseCase.updateProduct(eq(productId), any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.updateProduct(productId, productDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void updateProduct_WhenIdIsNull_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<?> response = productController.updateProduct(null, productDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldReturnNoContent() {
        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteProductUseCase).deleteProduct(productId);
    }

    @Test
    void deleteProduct_WhenIdIsNull_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<?> response = productController.deleteProduct(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteProduct_WhenBusinessException_ShouldReturnUnprocessableEntity() {
        // Arrange
        doThrow(new BusinessException("Error de negocio")).when(deleteProductUseCase).deleteProduct(productId);

        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
} 