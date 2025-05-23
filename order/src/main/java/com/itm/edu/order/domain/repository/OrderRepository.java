package com.itm.edu.order.domain.repository;

import com.itm.edu.order.domain.model.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    List<Order> findAll();
    void deleteById(UUID id);
} 