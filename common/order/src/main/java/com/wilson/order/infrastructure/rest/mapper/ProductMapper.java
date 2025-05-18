package com.wilson.order.infrastructure.rest.mapper;

import com.wilson.order.domain.model.Product;
import com.wilson.order.infrastructure.rest.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    
    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    public Product toDomain(ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();
    }
} 