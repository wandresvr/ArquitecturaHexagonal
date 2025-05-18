package com.itm.edu.order.application.ports.outputs;

import com.itm.edu.order.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID orderId);
    List<Order> findAll();
    void deleteById(UUID orderId);
    Order update(Order order);
} 