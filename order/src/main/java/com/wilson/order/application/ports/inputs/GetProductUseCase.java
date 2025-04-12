package com.wilson.order.application.ports.inputs;

import com.wilson.order.domain.model.Product;

import java.util.List;
import java.util.UUID;

public interface GetProductUseCase {
    Product getProduct(UUID id);
    List<Product> getAllProducts();
} 