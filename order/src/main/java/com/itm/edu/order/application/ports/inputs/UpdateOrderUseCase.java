package com.itm.edu.order.application.ports.inputs;

import com.itm.edu.order.domain.model.Order;
import java.util.UUID;

public interface UpdateOrderUseCase {
    Order updateOrder(UUID id, Order orderDetails);
} 