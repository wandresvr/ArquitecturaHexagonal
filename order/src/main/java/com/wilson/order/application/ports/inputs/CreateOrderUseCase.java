package com.wilson.order.application.ports.inputs;
import com.wilson.order.domain.model.Order;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface CreateOrderUseCase {
    Order createOrder(String customerName, Map<UUID, BigDecimal> ingredientQuantities);
} 
