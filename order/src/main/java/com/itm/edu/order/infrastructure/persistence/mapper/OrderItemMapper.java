package com.itm.edu.order.infrastructure.persistence.mapper;

import com.itm.edu.order.application.mapper.DomainMapper;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.infrastructure.persistence.entities.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper implements DomainMapper<OrderItem, OrderItemEntity> {
    private final ProductEntityMapper productMapper;

    public OrderItemMapper(ProductEntityMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public OrderItem toDomain(OrderItemEntity entity) {
        if (entity == null) return null;

        return OrderItem.builder()
                .product(productMapper.toDomain(entity.getProduct()))
                .quantity(entity.getQuantity())
                .build();
    }

    @Override
    public OrderItemEntity toEntity(OrderItem domain) {
        if (domain == null) return null;

        return OrderItemEntity.builder()
                .product(productMapper.toEntity(domain.getProduct()))
                .quantity(domain.getQuantity())
                .build();
    }

    public OrderItemEntity toEntityWithId(OrderItem domain, OrderItemEntity existingEntity) {
        OrderItemEntity entity = toEntity(domain);
        if (existingEntity != null) {
            entity.setId(existingEntity.getId());
        }
        return entity;
    }
} 