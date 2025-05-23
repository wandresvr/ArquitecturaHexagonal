package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.UpdateShippingAddressUseCase;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateShippingAddressService implements UpdateShippingAddressUseCase {
    private final OrderRepositoryPort orderRepository;

    @Override
    @Transactional
    public Order updateShippingAddress(UUID orderId, AddressShipping addressShipping) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new BusinessException("Orden no encontrada: " + orderId));

        Order updatedOrder = order.withUpdatedDeliveryAddress(addressShipping);
        return orderRepository.save(updatedOrder);
    }
} 