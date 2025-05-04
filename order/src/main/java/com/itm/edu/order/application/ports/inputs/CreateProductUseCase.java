package com.itm.edu.order.application.ports.inputs;

import com.itm.edu.order.domain.model.Product;

import java.math.BigDecimal;

public interface CreateProductUseCase {
    Product createProduct(String name, String description, BigDecimal price, Integer stock);
} 