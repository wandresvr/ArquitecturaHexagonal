package com.itm.edu.stock.domain.valueobjects;

import lombok.Value;

@Value
public class Unit {
    String value;

    public Unit(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("La unidad no puede estar vacía");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Unit of(String value) {
        return new Unit(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return value.equals(unit.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
