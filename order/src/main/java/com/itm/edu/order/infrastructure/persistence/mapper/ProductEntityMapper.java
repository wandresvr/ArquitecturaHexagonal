package com.itm.edu.order.infrastructure.persistence.mapper;

import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.infrastructure.persistence.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {
    
    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .build();
    }

    public ProductEntity toEntity(Product domain) {
        if (domain == null) return null;
        return ProductEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .stock(domain.getStock())
                .build();
    }
} 