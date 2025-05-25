package com.itm.edu.stock.infrastructure.persistence.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import com.itm.edu.stock.domain.entities.RecipeIngredient;

class RecipeIngredientJpaEntityTest {

    @Test
    void testToDomain_WithAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        String ingredientName = "Harina";
        BigDecimal quantity = new BigDecimal("500");
        String unit = "gramos";

        RecipeJpaEntity recipe = new RecipeJpaEntity();
        recipe.setId(recipeId);

        IngredientJpaEntity ingredient = new IngredientJpaEntity();
        ingredient.setId(ingredientId);
        ingredient.setName(ingredientName);

        RecipeIngredientJpaEntity entity = new RecipeIngredientJpaEntity();
        entity.setId(id);
        entity.setRecipe(recipe);
        entity.setIngredient(ingredient);
        entity.setQuantity(quantity);
        entity.setUnit(unit);

        // Act
        RecipeIngredient domain = entity.toDomain();

        // Assert
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals(recipeId, domain.getRecipeId());
        assertEquals(ingredientId, domain.getIngredientId());
        assertEquals(ingredientName, domain.getIngredientName());
        assertEquals(quantity, domain.getQuantity());
        assertEquals(unit, domain.getUnit());
    }

    @Test
    void testToDomain_WithNullRecipe() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID ingredientId = UUID.randomUUID();
        String ingredientName = "Harina";
        BigDecimal quantity = new BigDecimal("500");
        String unit = "gramos";

        IngredientJpaEntity ingredient = new IngredientJpaEntity();
        ingredient.setId(ingredientId);
        ingredient.setName(ingredientName);

        RecipeIngredientJpaEntity entity = new RecipeIngredientJpaEntity();
        entity.setId(id);
        entity.setRecipe(null);
        entity.setIngredient(ingredient);
        entity.setQuantity(quantity);
        entity.setUnit(unit);

        // Act
        RecipeIngredient domain = entity.toDomain();

        // Assert
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertNull(domain.getRecipeId());
        assertEquals(ingredientId, domain.getIngredientId());
        assertEquals(ingredientName, domain.getIngredientName());
        assertEquals(quantity, domain.getQuantity());
        assertEquals(unit, domain.getUnit());
    }

    @Test
    void testToDomain_WithNullIngredient() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID recipeId = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("500");
        String unit = "gramos";

        RecipeJpaEntity recipe = new RecipeJpaEntity();
        recipe.setId(recipeId);

        RecipeIngredientJpaEntity entity = new RecipeIngredientJpaEntity();
        entity.setId(id);
        entity.setRecipe(recipe);
        entity.setIngredient(null);
        entity.setQuantity(quantity);
        entity.setUnit(unit);

        // Act
        RecipeIngredient domain = entity.toDomain();

        // Assert
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals(recipeId, domain.getRecipeId());
        assertNull(domain.getIngredientId());
        assertNull(domain.getIngredientName());
        assertEquals(quantity, domain.getQuantity());
        assertEquals(unit, domain.getUnit());
    }

    @Test
    void testToDomain_WithMinimalFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal quantity = new BigDecimal("500");
        String unit = "gramos";

        RecipeIngredientJpaEntity entity = new RecipeIngredientJpaEntity();
        entity.setId(id);
        entity.setQuantity(quantity);
        entity.setUnit(unit);

        // Act
        RecipeIngredient domain = entity.toDomain();

        // Assert
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertNull(domain.getRecipeId());
        assertNull(domain.getIngredientId());
        assertNull(domain.getIngredientName());
        assertEquals(quantity, domain.getQuantity());
        assertEquals(unit, domain.getUnit());
    }
} 