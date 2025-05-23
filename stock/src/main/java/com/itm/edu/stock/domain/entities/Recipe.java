package com.itm.edu.stock.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    private UUID id;
    private String name;
    private String description;
    private String instructions;
    private Integer preparationTime;
    private String difficulty;
    private BigDecimal cost;
    private List<RecipeIngredient> recipeIngredients;

    public void updateCost(BigDecimal newCost) {
        if (newCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }
        this.cost = newCost;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("El ingrediente no puede ser nulo");
        }
        this.recipeIngredients.add(ingredient);
    }

    public void removeIngredient(RecipeIngredient ingredient) {
        this.recipeIngredients.remove(ingredient);
    }
}
