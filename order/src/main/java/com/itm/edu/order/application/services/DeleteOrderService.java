package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.DeleteOrderUseCase;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteOrderService implements DeleteOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public DeleteOrderService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        if (!orderRepositoryPort.findById(id).isPresent()) {
            throw new OrderNotFoundException("Order not found with id: " + id);
        }

        orderRepositoryPort.deleteById(id);
    }
} 