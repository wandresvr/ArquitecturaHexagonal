package com.itm.edu.order.application.ports.inputs;

import java.util.UUID;

public interface DeleteOrderUseCase {
    void deleteOrder(UUID id);
} 