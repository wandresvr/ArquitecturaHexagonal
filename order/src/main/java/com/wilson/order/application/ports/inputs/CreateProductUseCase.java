package com.wilson.order.application.ports.inputs;

import com.wilson.order.domain.model.Product;

import java.math.BigDecimal;

public interface CreateProductUseCase {
    Product createProduct(String name, String description, BigDecimal price, Integer stock);
} 