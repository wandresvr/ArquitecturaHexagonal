package com.itm.edu.stock.domain.valueobjects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnitTest {

    @Test
    void testUnitCreation() {
        // Arrange
        String value = "kg";

        // Act
        Unit unit = new Unit(value);

        // Assert
        assertEquals(value, unit.getValue());
    }

    @Test
    void testUnitEquality() {
        // Arrange
        Unit unit1 = new Unit("kg");
        Unit unit2 = new Unit("kg");
        Unit unit3 = new Unit("g");

        // Assert
        assertEquals(unit1, unit2);
        assertNotEquals(unit1, unit3);
        assertEquals(unit1.hashCode(), unit2.hashCode());
    }

    @Test
    void testUnitValidation() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Unit(null));
        assertThrows(IllegalArgumentException.class, () -> new Unit(""));
        assertThrows(IllegalArgumentException.class, () -> new Unit(" "));
    }
} 