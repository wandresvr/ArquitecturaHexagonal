package com.itm.edu.stock.domain.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import com.itm.edu.stock.domain.valueobjects.Quantity;
import com.itm.edu.stock.domain.valueobjects.Unit;

class RecipeTest {

    @Test
    void testRecipeCreation() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Recipe";
        String description = "Test Description";
        String instructions = "Test Instructions";
        Integer preparationTime = 30;
        String difficulty = "Easy";
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        BigDecimal cost = new BigDecimal("10.00");

        // Act
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setInstructions(instructions);
        recipe.setPreparationTime(preparationTime);
        recipe.setDifficulty(difficulty);
        recipe.setRecipeIngredients(recipeIngredients);
        recipe.setCost(cost);

        // Assert
        assertEquals(id, recipe.getId());
        assertEquals(name, recipe.getName());
        assertEquals(description, recipe.getDescription());
        assertEquals(instructions, recipe.getInstructions());
        assertEquals(preparationTime, recipe.getPreparationTime());
        assertEquals(difficulty, recipe.getDifficulty());
        assertEquals(recipeIngredients, recipe.getRecipeIngredients());
        assertEquals(cost, recipe.getCost());
    }
} 