package com.itm.edu.order.application.ports.inputs;

import java.util.UUID;

public interface DeleteProductUseCase {
    void deleteProduct(UUID id);
} 