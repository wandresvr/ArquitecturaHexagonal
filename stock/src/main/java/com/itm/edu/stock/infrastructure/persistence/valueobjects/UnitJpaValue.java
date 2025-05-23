package com.itm.edu.stock.infrastructure.persistence.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

@Embeddable
public class UnitJpaValue {
    @Column(name = "unit", nullable = false)
    private String value;

    public UnitJpaValue() {
    }

    public UnitJpaValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
} 