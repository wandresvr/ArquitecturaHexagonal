package com.itm.edu.stock.domain.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
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
        Recipe recipe = Recipe.builder()
            .id(id)
            .name(name)
            .description(description)
            .instructions(instructions)
            .preparationTime(preparationTime)
            .difficulty(difficulty)
            .recipeIngredients(recipeIngredients)
            .cost(cost)
            .build();

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

    @Test
    void testRecipeWithIngredients() {
        // Arrange
        List<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(RecipeIngredient.builder()
            .id(UUID.randomUUID())
            .ingredientId(UUID.randomUUID())
            .ingredientName("Harina")
            .quantity(BigDecimal.valueOf(500))
            .unit("gramos")
            .build());

        // Act
        Recipe recipe = Recipe.builder()
            .id(UUID.randomUUID())
            .name("Pan casero")
            .recipeIngredients(ingredients)
            .build();

        // Assert
        assertNotNull(recipe.getRecipeIngredients());
        assertEquals(1, recipe.getRecipeIngredients().size());
        assertEquals("Harina", recipe.getRecipeIngredients().get(0).getIngredientName());
    }

    @Test
    void testRecipeWithNullValues() {
        // Act
        Recipe recipe = Recipe.builder().build();

        // Assert
        assertNull(recipe.getId());
        assertNull(recipe.getName());
        assertNull(recipe.getDescription());
        assertNull(recipe.getInstructions());
        assertNull(recipe.getPreparationTime());
        assertNull(recipe.getDifficulty());
        assertNull(recipe.getCost());
        assertNotNull(recipe.getRecipeIngredients(), "La lista de ingredientes no debe ser nula");
        assertTrue(recipe.getRecipeIngredients().isEmpty(), "La lista de ingredientes debe estar vacía");
    }

    @Test
    void testRecipeWithCostUpdate() {
        // Arrange
        Recipe recipe = Recipe.builder()
            .id(UUID.randomUUID())
            .name("Test Recipe")
            .cost(BigDecimal.valueOf(10.00))
            .build();

        // Act
        Recipe updatedRecipe = recipe.withCost(BigDecimal.valueOf(15.00));

        // Assert
        assertEquals(BigDecimal.valueOf(15.00), updatedRecipe.getCost());
        assertEquals(recipe.getId(), updatedRecipe.getId());
        assertEquals(recipe.getName(), updatedRecipe.getName());
    }

    @Test
    void testRecipeWithIngredientsUpdate() {
        // Arrange
        Recipe recipe = Recipe.builder()
            .id(UUID.randomUUID())
            .name("Test Recipe")
            .recipeIngredients(new ArrayList<>())
            .build();

        List<RecipeIngredient> newIngredients = new ArrayList<>();
        newIngredients.add(RecipeIngredient.builder()
            .id(UUID.randomUUID())
            .ingredientId(UUID.randomUUID())
            .ingredientName("Azúcar")
            .quantity(BigDecimal.valueOf(200))
            .unit("gramos")
            .build());

        // Act
        Recipe updatedRecipe = recipe.withRecipeIngredients(newIngredients);

        // Assert
        assertEquals(1, updatedRecipe.getRecipeIngredients().size());
        assertEquals("Azúcar", updatedRecipe.getRecipeIngredients().get(0).getIngredientName());
        assertEquals(recipe.getId(), updatedRecipe.getId());
        assertEquals(recipe.getName(), updatedRecipe.getName());
    }

    @Test
    void testRecipeBuilder() {
        // Act
        Recipe recipe = Recipe.builder()
            .id(UUID.randomUUID())
            .name("Test Recipe")
            .description("Test Description")
            .instructions("Test Instructions")
            .preparationTime(30)
            .difficulty("Easy")
            .recipeIngredients(new ArrayList<>())
            .cost(BigDecimal.valueOf(10.00))
            .build();

        // Assert
        assertNotNull(recipe);
        assertNotNull(recipe.getId());
        assertEquals("Test Recipe", recipe.getName());
        assertEquals("Test Description", recipe.getDescription());
        assertEquals("Test Instructions", recipe.getInstructions());
        assertEquals(30, recipe.getPreparationTime());
        assertEquals("Easy", recipe.getDifficulty());
        assertTrue(recipe.getRecipeIngredients().isEmpty());
        assertEquals(BigDecimal.valueOf(10.00), recipe.getCost());
    }

    @Test
    void testWithCost() {
        // Arrange
        UUID id = UUID.randomUUID();
        BigDecimal cost = new BigDecimal("10.00");
        BigDecimal newCost = new BigDecimal("20.00");

        // Act
        Recipe recipe = Recipe.builder()
            .id(id)
            .cost(cost)
            .build();

        Recipe updatedRecipe = recipe.withCost(newCost);

        // Assert
        assertEquals(id, updatedRecipe.getId());
        assertEquals(newCost, updatedRecipe.getCost());
        assertEquals(cost, recipe.getCost()); // Original should be unchanged
    }

    @Test
    void testWithRecipeIngredients() {
        // Arrange
        UUID id = UUID.randomUUID();
        List<RecipeIngredient> ingredients = Arrays.asList(
            RecipeIngredient.builder()
                .id(UUID.randomUUID())
                .ingredientName("Harina")
                .quantity(new BigDecimal("500"))
                .unit("gramos")
                .build()
        );
        List<RecipeIngredient> newIngredients = Arrays.asList(
            RecipeIngredient.builder()
                .id(UUID.randomUUID())
                .ingredientName("Azúcar")
                .quantity(new BigDecimal("200"))
                .unit("gramos")
                .build()
        );

        // Act
        Recipe recipe = Recipe.builder()
            .id(id)
            .recipeIngredients(ingredients)
            .build();

        Recipe updatedRecipe = recipe.withRecipeIngredients(newIngredients);

        // Assert
        assertEquals(id, updatedRecipe.getId());
        assertEquals(newIngredients, updatedRecipe.getRecipeIngredients());
        assertEquals(ingredients, recipe.getRecipeIngredients()); // Original should be unchanged
    }

    @Test
    void testWithRecipeIngredients_Null() {
        // Arrange
        UUID id = UUID.randomUUID();
        List<RecipeIngredient> ingredients = Arrays.asList(
            RecipeIngredient.builder()
                .id(UUID.randomUUID())
                .ingredientName("Harina")
                .quantity(new BigDecimal("500"))
                .unit("gramos")
                .build()
        );

        // Act
        Recipe recipe = Recipe.builder()
            .id(id)
            .recipeIngredients(ingredients)
            .build();

        Recipe updatedRecipe = recipe.withRecipeIngredients(null);

        // Assert
        assertEquals(id, updatedRecipe.getId());
        assertTrue(updatedRecipe.getRecipeIngredients().isEmpty());
        assertEquals(ingredients, recipe.getRecipeIngredients()); // Original should be unchanged
    }

    @Test
    void testBuilderDefaults() {
        // Act
        Recipe recipe = Recipe.builder().build();

        // Assert
        assertNull(recipe.getId());
        assertNull(recipe.getName());
        assertNull(recipe.getDescription());
        assertNull(recipe.getInstructions());
        assertNull(recipe.getPreparationTime());
        assertNull(recipe.getDifficulty());
        assertNull(recipe.getCost());
        assertNotNull(recipe.getRecipeIngredients());
        assertTrue(recipe.getRecipeIngredients().isEmpty());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Test Recipe";
        String description = "Test Description";
        String instructions = "Test Instructions";
        Integer preparationTime = 30;
        String difficulty = "Easy";
        BigDecimal cost = new BigDecimal("10.00");
        List<RecipeIngredient> ingredients = Arrays.asList(
            RecipeIngredient.builder()
                .id(UUID.randomUUID())
                .ingredientName("Harina")
                .quantity(new BigDecimal("500"))
                .unit("gramos")
                .build()
        );

        // Act
        Recipe recipe = new Recipe(
            id, name, description, instructions,
            preparationTime, difficulty, cost, ingredients);

        // Assert
        assertEquals(id, recipe.getId());
        assertEquals(name, recipe.getName());
        assertEquals(description, recipe.getDescription());
        assertEquals(instructions, recipe.getInstructions());
        assertEquals(preparationTime, recipe.getPreparationTime());
        assertEquals(difficulty, recipe.getDifficulty());
        assertEquals(cost, recipe.getCost());
        assertEquals(ingredients, recipe.getRecipeIngredients());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Recipe recipe = new Recipe();

        // Assert
        assertNull(recipe.getId());
        assertNull(recipe.getName());
        assertNull(recipe.getDescription());
        assertNull(recipe.getInstructions());
        assertNull(recipe.getPreparationTime());
        assertNull(recipe.getDifficulty());
        assertNull(recipe.getCost());
        assertNotNull(recipe.getRecipeIngredients());
        assertTrue(recipe.getRecipeIngredients().isEmpty());
    }
} 