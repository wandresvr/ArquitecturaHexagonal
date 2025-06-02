package com.itm.edu.order.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class OrderItem {
    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal calculateValue() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public static OrderItem create(Product product, int quantity) {
        return new OrderItem(product, quantity);
    }
} 