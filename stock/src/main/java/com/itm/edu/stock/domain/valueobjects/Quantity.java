package com.itm.edu.stock.domain.valueobjects;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class Quantity {
    BigDecimal value;

    public static Quantity of(BigDecimal value) {
        return new Quantity(value);
    }

    public Quantity(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.value = value;
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value.add(other.value));
    }

    public Quantity subtract(Quantity other) {
        return new Quantity(this.value.subtract(other.value));
    }

    public boolean isLessThan(Quantity other) {
        return this.value.compareTo(other.value) < 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return value.compareTo(quantity.value) == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
