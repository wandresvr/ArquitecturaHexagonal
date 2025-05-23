package com.itm.edu.stock.infrastructure.persistence.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.math.BigDecimal;

@Embeddable
public class QuantityJpaValue {
    @Column(name = "quantity", nullable = false)
    private BigDecimal value;

    public QuantityJpaValue() {
    }

    public QuantityJpaValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
} 