package com.itm.edu.order.application.ports.inputs;

import com.itm.edu.order.domain.model.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetOrderUseCase {
    Optional<Order> getOrder(UUID id);
    List<Order> getAllOrders();
} 