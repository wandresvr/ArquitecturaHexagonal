package com.itm.edu.stock.domain.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.itm.edu.stock.domain.valueobjects.Quantity;
import com.itm.edu.stock.domain.valueobjects.Unit;

class RecipeIngredientTest {

    @Test
    void whenCreateRecipeIngredient_thenSuccess() {
        UUID id = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());
        ingredient.setName("Flour");
        Quantity quantity = new Quantity(new BigDecimal("500"));
        Unit unit = new Unit("g");

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(id);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(quantity);
        recipeIngredient.setUnit(unit);

        assertEquals(id, recipeIngredient.getId());
        assertEquals(ingredient.getId(), recipeIngredient.getIngredient().getId());
        assertEquals(quantity.getValue(), recipeIngredient.getQuantity().getValue());
        assertEquals(unit.getValue(), recipeIngredient.getUnit().getValue());
    }

    @Test
    void whenCreateRecipeIngredientWithConstructor_thenSuccess() {
        UUID id = UUID.randomUUID();
        Recipe recipe = new Recipe();
        recipe.setId(UUID.randomUUID());
        recipe.setName("Pizza");
        
        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());
        ingredient.setName("Flour");
        
        Quantity quantity = new Quantity(new BigDecimal("500"));
        Unit unit = new Unit("g");

        RecipeIngredient recipeIngredient = new RecipeIngredient(id, recipe, ingredient, quantity, unit);

        assertEquals(id, recipeIngredient.getId());
        assertEquals(recipe.getId(), recipeIngredient.getRecipe().getId());
        assertEquals(ingredient.getId(), recipeIngredient.getIngredient().getId());
        assertEquals(quantity.getValue(), recipeIngredient.getQuantity().getValue());
        assertEquals(unit.getValue(), recipeIngredient.getUnit().getValue());
    }
} 