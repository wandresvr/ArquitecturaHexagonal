package com.wilson.order.domain.valueobjects;

import com.wilson.order.domain.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTotal {
    private Product product;
    private BigDecimal quantity;    // Changed to BigDecimal for precise measurements
    
    public BigDecimal calculateValue() {
        return product.getPrice().multiply(quantity);
    }
} 