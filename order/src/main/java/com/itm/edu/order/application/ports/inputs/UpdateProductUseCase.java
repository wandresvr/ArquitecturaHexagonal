package com.itm.edu.order.application.ports.inputs;

import com.itm.edu.order.domain.model.Product;
import java.util.UUID;

public interface UpdateProductUseCase {
    Product updateProduct(UUID id, Product product);
} 