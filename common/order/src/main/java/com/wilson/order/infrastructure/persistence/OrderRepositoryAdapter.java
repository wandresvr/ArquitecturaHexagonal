package com.wilson.order.infrastructure.persistence;

import com.wilson.order.domain.model.Order;
import com.wilson.order.application.ports.outputs.OrderRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryAdapter implements OrderRepositoryPort {
    private final Map<UUID, Order> orders = new HashMap<>();

    @Override
    public Order save(Order order) {
        if (order.getOrderId() == null) {
            order.setOrderId(UUID.randomUUID());
        }
        orders.put(order.getOrderId(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }
} 