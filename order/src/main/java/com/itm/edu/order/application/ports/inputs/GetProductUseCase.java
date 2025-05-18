package com.itm.edu.order.application.ports.inputs;

import com.itm.edu.order.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetProductUseCase {
    Optional<Product> getProduct(UUID id);
    List<Product> getAllProducts();
} 