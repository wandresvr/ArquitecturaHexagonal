package com.itm.edu.order.application.ports.inputs;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface CreateOrderUseCase {
    Order createOrder(Client client, Map<UUID, BigDecimal> productQuantities, AddressShipping shippingAddress);
} 
