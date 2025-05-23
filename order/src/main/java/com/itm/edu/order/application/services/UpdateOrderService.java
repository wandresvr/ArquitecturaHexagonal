package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.UpdateOrderUseCase;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOrderService implements UpdateOrderUseCase {
    private final OrderRepositoryPort orderRepository;

    @Override
    @Transactional
    public Order updateOrder(UUID orderId, Order orderDetails) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("Orden no encontrada con ID: " + orderId));

        Order updatedOrder = existingOrder;
        
        if (orderDetails.getOrderStatus() != null) {
            updatedOrder = updatedOrder.withUpdatedStatus(orderDetails.getOrderStatus());
        }
        
        if (orderDetails.getClient() != null) {
            updatedOrder = updatedOrder.withUpdatedClient(orderDetails.getClient());
        }

        if (orderDetails.getDeliveryAddress() != null) {
            updatedOrder = updatedOrder.withUpdatedDeliveryAddress(orderDetails.getDeliveryAddress());
        }

        return orderRepository.save(updatedOrder);
    }
} 