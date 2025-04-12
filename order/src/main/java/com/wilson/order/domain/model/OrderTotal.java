package com.wilson.order.domain.model;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class OrderTotal {
    BigDecimal amount;
    String currency;

    public OrderTotal(BigDecimal amount, String currency) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto total no puede ser negativo");
        }
        this.amount = amount;
        this.currency = currency;
    }
} 