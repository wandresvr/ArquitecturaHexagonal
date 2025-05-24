package com.itm.edu.stock.domain.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {

    @Test
    void testCreateIngredient() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Harina";
        String description = "Harina de trigo";
        BigDecimal quantity = new BigDecimal("1000");
        String unit = "gramos";
        BigDecimal price = new BigDecimal("2.50");
        String supplier = "Proveedor A";

        // Act
        Ingredient ingredient = Ingredient.builder()
            .id(id)
            .name(name)
            .description(description)
            .quantity(quantity)
            .unit(unit)
            .price(price)
            .supplier(supplier)
            .build();

        // Assert
        assertEquals(id, ingredient.getId());
        assertEquals(name, ingredient.getName());
        assertEquals(description, ingredient.getDescription());
        assertEquals(quantity, ingredient.getQuantity());
        assertEquals(unit, ingredient.getUnit());
        assertEquals(price, ingredient.getPrice());
        assertEquals(supplier, ingredient.getSupplier());
    }

    @Test
    void testIngredientWithNullValues() {
        // Act
        Ingredient ingredient = Ingredient.builder().build();

        // Assert
        assertNull(ingredient.getId());
        assertNull(ingredient.getName());
        assertNull(ingredient.getDescription());
        assertNull(ingredient.getQuantity());
        assertNull(ingredient.getUnit());
        assertNull(ingredient.getPrice());
        assertNull(ingredient.getSupplier());
    }

    @Test
    void testIngredientWithUpdates() {
        // Arrange
        Ingredient ingredient = Ingredient.builder()
            .id(UUID.randomUUID())
            .name("Azúcar")
            .quantity(new BigDecimal("500"))
            .unit("gramos")
            .price(new BigDecimal("1.50"))
            .build();

        // Act
        BigDecimal newQuantity = new BigDecimal("750");
        BigDecimal newPrice = new BigDecimal("2.00");
        
        Ingredient updatedIngredient = ingredient.toBuilder()
            .quantity(newQuantity)
            .price(newPrice)
            .build();

        // Assert
        assertEquals(ingredient.getId(), updatedIngredient.getId());
        assertEquals(ingredient.getName(), updatedIngredient.getName());
        assertEquals(newQuantity, updatedIngredient.getQuantity());
        assertEquals(newPrice, updatedIngredient.getPrice());
    }
} 