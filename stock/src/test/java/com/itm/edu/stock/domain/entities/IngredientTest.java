package com.itm.edu.stock.domain.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    @Test
    void testCreateIngredient() {
        UUID id = UUID.randomUUID();
        String name = "Harina";
        String description = "Harina de trigo";
        BigDecimal quantity = new BigDecimal("1000");
        String unit = "gramos";
        BigDecimal price = new BigDecimal("2.50");
        String supplier = "Proveedor A";

        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName(name);
        ingredient.setDescription(description);
        ingredient.setQuantity(quantity);
        ingredient.setUnit(unit);
        ingredient.setPrice(price);
        ingredient.setSupplier(supplier);

        assertEquals(id, ingredient.getId());
        assertEquals(name, ingredient.getName());
        assertEquals(description, ingredient.getDescription());
        assertEquals(quantity, ingredient.getQuantity());
        assertEquals(unit, ingredient.getUnit());
        assertEquals(price, ingredient.getPrice());
        assertEquals(supplier, ingredient.getSupplier());
    }

    @Test
    void testCreateIngredientWithConstructor() {
        UUID id = UUID.randomUUID();
        String name = "Azúcar";
        String description = "Azúcar refinada";
        BigDecimal quantity = new BigDecimal("500");
        String unit = "gramos";
        BigDecimal price = new BigDecimal("1.50");
        String supplier = "Proveedor B";

        Ingredient ingredient = new Ingredient(id, name, description, quantity, unit, price, supplier);

        assertEquals(id, ingredient.getId());
        assertEquals(name, ingredient.getName());
        assertEquals(description, ingredient.getDescription());
        assertEquals(quantity, ingredient.getQuantity());
        assertEquals(unit, ingredient.getUnit());
        assertEquals(price, ingredient.getPrice());
        assertEquals(supplier, ingredient.getSupplier());
    }
} 