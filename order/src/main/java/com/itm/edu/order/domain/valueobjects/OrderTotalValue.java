package com.itm.edu.order.domain.valueobjects;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTotalValue that = (OrderTotalValue) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
} 