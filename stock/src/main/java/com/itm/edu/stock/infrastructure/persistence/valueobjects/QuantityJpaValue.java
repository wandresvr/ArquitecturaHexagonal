package com.itm.edu.stock.infrastructure.persistence.valueobjects;

import com.itm.edu.stock.domain.valueobjects.Quantity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.math.BigDecimal;

@Embeddable
public class QuantityJpaValue {
    @Column(name = "quantity", nullable = false)
    private BigDecimal value;

    public QuantityJpaValue() {
    }

    public QuantityJpaValue(Quantity quantity) {
        this.value = quantity.getValue();
    }

    public Quantity toDomain() {
        return new Quantity(value);
    }

    public static QuantityJpaValue fromDomain(Quantity quantity) {
        if (quantity == null) return null;
        return new QuantityJpaValue(quantity);
    }
} 