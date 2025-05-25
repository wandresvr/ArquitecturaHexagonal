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

    @Test
    void testWithQuantity() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal originalQuantity = new BigDecimal("500");
        BigDecimal newQuantity = new BigDecimal("750");
        
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
            .id(id)
            .quantity(originalQuantity)
            .build();

        // Act
        RecipeIngredient updatedRecipeIngredient = recipeIngredient.withQuantity(newQuantity);

        // Assert
        assertEquals(newQuantity, updatedRecipeIngredient.getQuantity());
        assertEquals(originalQuantity, recipeIngredient.getQuantity()); // Original should be unchanged
        assertEquals(id, updatedRecipeIngredient.getId()); // Other fields should remain the same
    }

    @Test
    void testWithUnit() {
        // Arrange
        UUID id = UUID.randomUUID();
        String originalUnit = "gramos";
        String newUnit = "kilogramos";
        
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
            .id(id)
            .unit(originalUnit)
            .build();

        // Act
        RecipeIngredient updatedRecipeIngredient = recipeIngredient.withUnit(newUnit);

        // Assert
        assertEquals(newUnit, updatedRecipeIngredient.getUnit());
        assertEquals(originalUnit, recipeIngredient.getUnit()); // Original should be unchanged
        assertEquals(id, updatedRecipeIngredient.getId()); // Other fields should remain the same
    }

    @Test
    void testWithQuantity_NullValue() {
        // Arrange
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
            .quantity(new BigDecimal("500"))
            .build();

        // Act
        RecipeIngredient updatedRecipeIngredient = recipeIngredient.withQuantity(null);

        // Assert
        assertNull(updatedRecipeIngredient.getQuantity());
    }

    @Test
    void testWithUnit_NullValue() {
        // Arrange
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
            .unit("gramos")
            .build();

        // Act
        RecipeIngredient updatedRecipeIngredient = recipeIngredient.withUnit(null);

        // Assert
        assertNull(updatedRecipeIngredient.getUnit());
    }

    @Test
    void testBuilderDefaults() {
        // Act
        RecipeIngredient recipeIngredient = RecipeIngredient.builder().build();

        // Assert
        assertNull(recipeIngredient.getId());
        assertNull(recipeIngredient.getRecipeId());
        assertNull(recipeIngredient.getIngredientId());
        assertNull(recipeIngredient.getIngredientName());
        assertNull(recipeIngredient.getQuantity());
        assertNull(recipeIngredient.getUnit());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        String ingredientName = "Harina";
        BigDecimal quantity = new BigDecimal("500");
        String unit = "gramos";

        // Act
        RecipeIngredient recipeIngredient = new RecipeIngredient(
            id, recipeId, ingredientId, ingredientName, quantity, unit);

        // Assert
        assertEquals(id, recipeIngredient.getId());
        assertEquals(recipeId, recipeIngredient.getRecipeId());
        assertEquals(ingredientId, recipeIngredient.getIngredientId());
        assertEquals(ingredientName, recipeIngredient.getIngredientName());
        assertEquals(quantity, recipeIngredient.getQuantity());
        assertEquals(unit, recipeIngredient.getUnit());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        RecipeIngredient recipeIngredient = new RecipeIngredient();

        // Assert
        assertNull(recipeIngredient.getId());
        assertNull(recipeIngredient.getRecipeId());
        assertNull(recipeIngredient.getIngredientId());
        assertNull(recipeIngredient.getIngredientName());
        assertNull(recipeIngredient.getQuantity());
        assertNull(recipeIngredient.getUnit());
    }
} 