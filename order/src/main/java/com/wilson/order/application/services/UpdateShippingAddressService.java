package com.wilson.order.application.services;

import com.wilson.order.application.ports.inputs.UpdateShippingAddressUseCase;
import com.wilson.order.application.ports.outputs.OrderRepositoryPort;
import com.wilson.order.domain.valueobjects.AddressShipping;
import com.wilson.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateShippingAddressService implements UpdateShippingAddressUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    public Order updateShippingAddress(UUID orderId, AddressShipping addressShipping) {
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setDeliveryAddress(addressShipping);
        return orderRepositoryPort.save(order);
    }
} 