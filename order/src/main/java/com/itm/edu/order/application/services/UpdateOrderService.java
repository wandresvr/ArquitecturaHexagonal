package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.UpdateOrderUseCase;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.exception.OrderNotFoundException;
import com.itm.edu.order.domain.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateOrderService implements UpdateOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public UpdateOrderService(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    @Transactional
    public Order updateOrder(UUID id, Order orderDetails) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        if (orderDetails == null) {
            throw new IllegalArgumentException("Order details cannot be null");
        }

        Order existingOrder = orderRepositoryPort.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        // Actualizar los campos necesarios
        existingOrder.setOrderStatus(orderDetails.getOrderStatus());
        if (orderDetails.getDeliveryAddress() != null) {
            existingOrder.setDeliveryAddress(orderDetails.getDeliveryAddress());
        }
        if (orderDetails.getClient() != null) {
            existingOrder.setClient(orderDetails.getClient());
        }

        return orderRepositoryPort.update(existingOrder);
    }
} 