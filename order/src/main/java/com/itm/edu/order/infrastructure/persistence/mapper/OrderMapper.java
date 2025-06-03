package com.itm.edu.order.infrastructure.persistence.mapper;

import com.itm.edu.order.application.mapper.DomainMapper;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.infrastructure.persistence.entities.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper implements DomainMapper<Order, OrderEntity> {
    private final OrderItemMapper orderItemMapper;
    private final ClientMapper clientMapper;

    public OrderMapper(OrderItemMapper orderItemMapper, ClientMapper clientMapper) {
        this.orderItemMapper = orderItemMapper;
        this.clientMapper = clientMapper;
    }

    @Override
    public Order toDomain(OrderEntity entity) {
        if (entity == null) return null;
        
        return Order.builder()
                .orderId(entity.getOrderId())
                .products(entity.getProducts().stream()
                        .map(orderItemMapper::toDomain)
                        .collect(Collectors.toList()))
                .orderStatus(entity.getOrderStatus())
                .orderDate(entity.getOrderDate())
                .deliveryAddress(entity.getDeliveryAddress())
                .total(entity.getTotal())
                .client(clientMapper.toDomain(entity.getClient()))
                .build();
    }

    @Override
    public OrderEntity toEntity(Order domain) {
        if (domain == null) return null;

        OrderEntity orderEntity = OrderEntity.builder()
                .orderId(domain.getOrderId())
                .orderStatus(domain.getOrderStatus())
                .orderDate(domain.getOrderDate())
                .deliveryAddress(domain.getDeliveryAddress())
                .total(domain.getTotal())
                .client(clientMapper.toEntity(domain.getClient()))
                .build();

        // Establecer la relación bidireccional a nivel de infraestructura
        orderEntity.setProducts(domain.getProducts().stream()
                .map(item -> orderItemMapper.toEntityWithOrder(item, orderEntity))
                .collect(Collectors.toList()));

        return orderEntity;
    }
} 