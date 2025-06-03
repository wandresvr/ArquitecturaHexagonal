package com.itm.edu.order.infrastructure.persistence.mapper;

import com.itm.edu.order.application.mapper.DomainMapper;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.infrastructure.persistence.entities.OrderItemEntity;
import com.itm.edu.order.infrastructure.persistence.entities.OrderEntity;
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

    public OrderItemEntity toEntityWithOrder(OrderItem domain, OrderEntity order) {
        if (domain == null) return null;

        OrderItemEntity entity = OrderItemEntity.builder()
                .product(productMapper.toEntity(domain.getProduct()))
                .quantity(domain.getQuantity())
                .build();
        
        entity.setOrder(order);
        return entity;
    }

    public OrderItemEntity toEntityWithId(OrderItem domain, OrderItemEntity existingEntity) {
        if (domain == null) return null;

        OrderItemEntity entity = toEntity(domain);
        if (existingEntity != null) {
            entity.setId(existingEntity.getId());
            entity.setOrder(existingEntity.getOrder());
        }
        return entity;
    }
} 