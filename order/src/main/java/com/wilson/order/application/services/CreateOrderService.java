package com.wilson.order.application.services;

import com.wilson.order.application.ports.inputs.CreateOrderUseCase;
import com.wilson.order.application.ports.outputs.OrderRepositoryPort;
import com.wilson.order.domain.model.Client;
import com.wilson.order.domain.model.Order;
import com.wilson.order.domain.valueobjects.AddressShipping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    public Order createOrder(Client client, Map<UUID, BigDecimal> productQuantities, AddressShipping shippingAddress) {
        Order order = Order.builder()
                .orderStatus("CREATED")
                .orderDate(java.time.LocalDateTime.now())
                .deliveryAddress(shippingAddress)
                .client(client)
                .build();

        return orderRepositoryPort.save(order);
    }
}