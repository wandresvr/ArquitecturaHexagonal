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
    void whenCreateRecipe_thenSuccess() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        String name = "Pizza";
        String description = "Homemade pizza";

        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setProductId(productId);
        recipe.setName(name);
        recipe.setDescription(description);

        assertEquals(id, recipe.getId());
        assertEquals(productId, recipe.getProductId());
        assertEquals(name, recipe.getName());
        assertEquals(description, recipe.getDescription());
        assertTrue(recipe.getIngredients().isEmpty());
    }

    @Test
    void whenAddIngredient_thenSuccess() {
        Recipe recipe = new Recipe();
        recipe.setId(UUID.randomUUID());
        recipe.setProductId(UUID.randomUUID());
        recipe.setName("Pizza");

        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());
        ingredient.setName("Flour");

        recipe.addIngredient(ingredient, new BigDecimal("500"), "g");

        assertEquals(1, recipe.getIngredients().size());
        RecipeIngredient recipeIngredient = recipe.getIngredients().get(0);
        assertEquals(ingredient.getId(), recipeIngredient.getIngredient().getId());
        assertEquals(new BigDecimal("500"), recipeIngredient.getQuantity().getValue());
        assertEquals("g", recipeIngredient.getUnit().getValue());
    }

    @Test
    void whenCreateRecipeWithConstructor_thenSuccess() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        String name = "Pizza";
        String description = "Homemade pizza";
        List<RecipeIngredient> ingredients = new ArrayList<>();

        Recipe recipe = new Recipe(id, productId, name, description, ingredients);

        assertEquals(id, recipe.getId());
        assertEquals(productId, recipe.getProductId());
        assertEquals(name, recipe.getName());
        assertEquals(description, recipe.getDescription());
        assertTrue(recipe.getIngredients().isEmpty());
    }
} 