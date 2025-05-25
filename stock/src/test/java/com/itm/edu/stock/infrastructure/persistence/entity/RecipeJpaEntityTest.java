package com.itm.edu.stock.infrastructure.persistence.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Arrays;
import java.util.ArrayList;
import com.itm.edu.stock.domain.entities.Recipe;

class RecipeJpaEntityTest {

    @Test
    void testToDomain_WithAllFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Torta de Chocolate";
        String description = "Torta esponjosa de chocolate";
        String instructions = "1. Mezclar ingredientes\n2. Hornear";
        Integer preparationTime = 30;
        String difficulty = "Medio";
        BigDecimal cost = new BigDecimal("10.00");

        RecipeJpaEntity entity = new RecipeJpaEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setDescription(description);
        entity.setInstructions(instructions);
        entity.setPreparationTime(preparationTime);
        entity.setDifficulty(difficulty);
        entity.setCost(cost);

        // Act
        Recipe domain = entity.toDomain();

        // Assert
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals(name, domain.getName());
        assertEquals(description, domain.getDescription());
        assertEquals(instructions, domain.getInstructions());
        assertEquals(preparationTime, domain.getPreparationTime());
        assertEquals(difficulty, domain.getDifficulty());
        assertEquals(cost, domain.getCost());
        assertNotNull(domain.getRecipeIngredients());
        assertTrue(domain.getRecipeIngredients().isEmpty());
    }

    @Test
    void testToDomain_WithMinimalFields() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Torta de Chocolate";
        String difficulty = "Medio";

        RecipeJpaEntity entity = new RecipeJpaEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setDifficulty(difficulty);

        // Act
        Recipe domain = entity.toDomain();

        // Assert
        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals(name, domain.getName());
        assertEquals(difficulty, domain.getDifficulty());
        assertNull(domain.getDescription());
        assertNull(domain.getInstructions());
        assertNull(domain.getPreparationTime());
        assertNull(domain.getCost());
        assertNotNull(domain.getRecipeIngredients());
        assertTrue(domain.getRecipeIngredients().isEmpty());
    }

    @Test
    void testSetRecipeIngredients_WithValidIngredients() {
        // Arrange
        UUID recipeId = UUID.randomUUID();
        RecipeJpaEntity recipe = new RecipeJpaEntity();
        recipe.setId(recipeId);

        RecipeIngredientJpaEntity ingredient1 = new RecipeIngredientJpaEntity();
        ingredient1.setId(UUID.randomUUID());
        ingredient1.setQuantity(new BigDecimal("500"));
        ingredient1.setUnit("gramos");

        RecipeIngredientJpaEntity ingredient2 = new RecipeIngredientJpaEntity();
        ingredient2.setId(UUID.randomUUID());
        ingredient2.setQuantity(new BigDecimal("250"));
        ingredient2.setUnit("gramos");

        // Act
        recipe.setRecipeIngredients(Arrays.asList(ingredient1, ingredient2));

        // Assert
        assertEquals(2, recipe.getRecipeIngredients().size());
        assertEquals(recipe, recipe.getRecipeIngredients().get(0).getRecipe());
        assertEquals(recipe, recipe.getRecipeIngredients().get(1).getRecipe());
    }

    @Test
    void testSetRecipeIngredients_WithNullList() {
        // Arrange
        RecipeJpaEntity recipe = new RecipeJpaEntity();
        recipe.setRecipeIngredients(Arrays.asList(new RecipeIngredientJpaEntity())); // Add an initial ingredient

        // Act
        recipe.setRecipeIngredients(null);

        // Assert
        assertNotNull(recipe.getRecipeIngredients());
        assertTrue(recipe.getRecipeIngredients().isEmpty());
    }

    @Test
    void testSetRecipeIngredients_WithEmptyList() {
        // Arrange
        RecipeJpaEntity recipe = new RecipeJpaEntity();
        recipe.setRecipeIngredients(Arrays.asList(new RecipeIngredientJpaEntity())); // Add an initial ingredient

        // Act
        recipe.setRecipeIngredients(new ArrayList<>());

        // Assert
        assertNotNull(recipe.getRecipeIngredients());
        assertTrue(recipe.getRecipeIngredients().isEmpty());
    }

    @Test
    void testSetRecipeIngredients_ClearsPreviousIngredients() {
        // Arrange
        RecipeJpaEntity recipe = new RecipeJpaEntity();
        
        RecipeIngredientJpaEntity oldIngredient = new RecipeIngredientJpaEntity();
        oldIngredient.setId(UUID.randomUUID());
        recipe.setRecipeIngredients(Arrays.asList(oldIngredient));

        RecipeIngredientJpaEntity newIngredient = new RecipeIngredientJpaEntity();
        newIngredient.setId(UUID.randomUUID());

        // Act
        recipe.setRecipeIngredients(Arrays.asList(newIngredient));

        // Assert
        assertEquals(1, recipe.getRecipeIngredients().size());
        assertEquals(newIngredient.getId(), recipe.getRecipeIngredients().get(0).getId());
    }
} 