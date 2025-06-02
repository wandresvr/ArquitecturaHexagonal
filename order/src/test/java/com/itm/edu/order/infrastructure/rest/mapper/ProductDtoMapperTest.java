package com.itm.edu.order.infrastructure.rest.mapper;

import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.infrastructure.rest.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductDtoMapperTest {

    private ProductDtoMapper mapper;
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    @BeforeEach
    void setUp() {
        mapper = new ProductDtoMapper();
        productId = UUID.randomUUID();
        name = "Test Product";
        description = "Test Description";
        price = new BigDecimal("10.99");
        stock = 100;
    }

    @Test
    void shouldMapProductToDtoSuccessfully() {
        // Arrange
        Product product = Product.builder()
                .id(productId)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();

        // Act
        ProductDto dto = mapper.toDto(product);

        // Assert
        assertNotNull(dto);
        assertEquals(productId, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(description, dto.getDescription());
        assertEquals(price, dto.getPrice());
        assertEquals(stock, dto.getStock());
    }

    @Test
    void shouldReturnNullWhenMappingNullProductToDto() {
        // Act
        ProductDto dto = mapper.toDto(null);

        // Assert
        assertNull(dto);
    }

    @Test
    void shouldMapDtoToProductSuccessfully() {
        // Arrange
        ProductDto dto = ProductDto.builder()
                .id(productId)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();

        // Act
        Product product = mapper.toDomain(dto);

        // Assert
        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(stock, product.getStock());
    }

    @Test
    void shouldReturnNullWhenMappingNullDtoToProduct() {
        // Act
        Product product = mapper.toDomain(null);

        // Assert
        assertNull(product);
    }
} 