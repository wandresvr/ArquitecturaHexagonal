package com.itm.edu.order.infrastructure.rest.mapper;

import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.infrastructure.rest.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component("productDtoMapper")
public class ProductDtoMapper {
    
    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        
        return ProductDto.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .build();
    }

    public Product toDomain(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Product.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .price(dto.getPrice())
            .stock(dto.getStock())
            .build();
    }
} 