package com.wilson.stock.domain.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.wilson.stock.domain.valueobjects.Quantity;
import com.wilson.stock.domain.valueobjects.Unit;

class IngredientTest {

    @Test
    void whenCreateIngredient_thenSuccess() {
        UUID id = UUID.randomUUID();
        String name = "Flour";
        String description = "All purpose flour";
        Quantity quantity = new Quantity(new BigDecimal("1000"));
        Unit unit = new Unit("g");

        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName(name);
        ingredient.setDescription(description);
        ingredient.setQuantity(quantity);
        ingredient.setUnit(unit);

        assertEquals(id, ingredient.getId());
        assertEquals(name, ingredient.getName());
        assertEquals(description, ingredient.getDescription());
        assertEquals(quantity.getValue(), ingredient.getQuantity().getValue());
        assertEquals(unit.getValue(), ingredient.getUnit().getValue());
    }

    @Test
    void whenCreateIngredientWithConstructor_thenSuccess() {
        UUID id = UUID.randomUUID();
        String name = "Flour";
        String description = "All purpose flour";
        Quantity quantity = new Quantity(new BigDecimal("1000"));
        Unit unit = new Unit("g");

        Ingredient ingredient = new Ingredient(id, name, description, quantity, unit);

        assertEquals(id, ingredient.getId());
        assertEquals(name, ingredient.getName());
        assertEquals(description, ingredient.getDescription());
        assertEquals(quantity.getValue(), ingredient.getQuantity().getValue());
        assertEquals(unit.getValue(), ingredient.getUnit().getValue());
    }
} 