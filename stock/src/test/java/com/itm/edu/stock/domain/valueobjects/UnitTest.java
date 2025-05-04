package com.itm.edu.stock.domain.valueobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    @Test
    void whenCreateUnit_thenSuccess() {
        String value = "kg";
        Unit unit = new Unit();
        unit.setValue(value);
        assertEquals(value, unit.getValue());
    }

    @Test
    void whenCreateUnitWithConstructor_thenSuccess() {
        String value = "kg";
        Unit unit = new Unit(value);
        assertEquals(value, unit.getValue());
    }
} 