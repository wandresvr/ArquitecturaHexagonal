package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.GetOrderUseCase;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GetOrderService implements GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public GetOrderService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public Optional<Order> getOrder(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        return orderRepositoryPort.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepositoryPort.findAll();
    }
} 