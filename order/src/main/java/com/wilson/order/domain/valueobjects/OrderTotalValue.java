package com.wilson.order.domain.valueobjects;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class OrderTotalValue {
    BigDecimal amount;
    String currency;

    public OrderTotalValue(BigDecimal amount, String currency) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto total no puede ser negativo");
        }
        this.amount = amount;
        this.currency = currency;
    }
} 