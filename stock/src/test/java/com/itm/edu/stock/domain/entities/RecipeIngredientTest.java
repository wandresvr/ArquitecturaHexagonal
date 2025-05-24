package com.itm.edu.stock.domain.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class RecipeIngredientTest {

    @Test
    void testRecipeIngredientCreation() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        String ingredientName = "Harina";
        BigDecimal quantity = new BigDecimal("500");
        String unit = "gramos";

        // Act
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
            .id(id)
            .ingredientId(ingredientId)
            .ingredientName(ingredientName)
            .quantity(quantity)
            .unit(unit)
            .build();

        // Assert
        assertEquals(id, recipeIngredient.getId());
        assertEquals(ingredientId, recipeIngredient.getIngredientId());
        assertEquals(ingredientName, recipeIngredient.getIngredientName());
        assertEquals(quantity, recipeIngredient.getQuantity());
        assertEquals(unit, recipeIngredient.getUnit());
    }

    @Test
    void testRecipeIngredientWithNullValues() {
        // Act
        RecipeIngredient recipeIngredient = RecipeIngredient.builder().build();

        // Assert
        assertNull(recipeIngredient.getId());
        assertNull(recipeIngredient.getIngredientId());
        assertNull(recipeIngredient.getIngredientName());
        assertNull(recipeIngredient.getQuantity());
        assertNull(recipeIngredient.getUnit());
    }

    @Test
    void testRecipeIngredientWithUpdates() {
        // Arrange
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
            .id(UUID.randomUUID())
            .ingredientId(UUID.randomUUID())
            .ingredientName("Harina")
            .quantity(new BigDecimal("500"))
            .unit("gramos")
            .build();

        // Act
        BigDecimal newQuantity = new BigDecimal("750");
        String newUnit = "kilogramos";
        
        RecipeIngredient updatedIngredient = recipeIngredient.toBuilder()
            .quantity(newQuantity)
            .unit(newUnit)
            .build();

        // Assert
        assertEquals(recipeIngredient.getId(), updatedIngredient.getId());
        assertEquals(recipeIngredient.getIngredientId(), updatedIngredient.getIngredientId());
        assertEquals(recipeIngredient.getIngredientName(), updatedIngredient.getIngredientName());
        assertEquals(newQuantity, updatedIngredient.getQuantity());
        assertEquals(newUnit, updatedIngredient.getUnit());
    }
} 