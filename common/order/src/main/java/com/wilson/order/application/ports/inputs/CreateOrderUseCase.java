package com.wilson.order.application.ports.inputs;

import com.wilson.order.domain.model.Client;
import com.wilson.order.domain.model.Order;
import com.wilson.order.domain.valueobjects.AddressShipping;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface CreateOrderUseCase {
    Order createOrder(Client client, Map<UUID, BigDecimal> productQuantities, AddressShipping shippingAddress);
} 
