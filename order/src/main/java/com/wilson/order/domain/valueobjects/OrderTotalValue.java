package com.wilson.order.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor
public class OrderTotalValue {
    private BigDecimal amount;
    private String currency;

    public OrderTotalValue(BigDecimal amount, String currency) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto total no puede ser negativo");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public void addAmount(BigDecimal amountToAdd) {
        if (amountToAdd.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto a agregar no puede ser negativo");
        }
        this.amount = this.amount.add(amountToAdd);
    }

    public void subtractAmount(BigDecimal amountToSubtract) {
        if (amountToSubtract.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto a restar no puede ser negativo");
        }
        BigDecimal newAmount = this.amount.subtract(amountToSubtract);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto resultante no puede ser negativo");
        }
        this.amount = newAmount;
    }

    public static OrderTotalValue zero() {
        return new OrderTotalValue(BigDecimal.ZERO, "USD");
    }
} 