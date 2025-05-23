package com.itm.edu.order.application.ports.inputs;

import com.itm.edu.order.domain.model.Product;

import java.math.BigDecimal;
import java.util.UUID;

public interface CreateProductUseCase {
    Product createProduct(Product product);
} 