package com.itm.edu.stock.infrastructure.persistence.valueobjects;

import com.itm.edu.stock.domain.valueobjects.Unit;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

@Embeddable
public class UnitJpaValue {
    @Column(name = "unit", nullable = false)
    private String value;

    public UnitJpaValue() {
    }

    public UnitJpaValue(Unit unit) {
        this.value = unit.getValue();
    }

    public Unit toDomain() {
        return new Unit(value);
    }

    public static UnitJpaValue fromDomain(Unit unit) {
        if (unit == null) return null;
        return new UnitJpaValue(unit);
    }
} 