package com.itm.edu.order.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OrderItem {
    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal calculateValue() {
        if (product == null) {
            return BigDecimal.ZERO;
        }
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public static OrderItem create(Product product, int quantity) {
        return new OrderItem(product, quantity);
    }
} 