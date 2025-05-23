package com.itm.edu.stock.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.With;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@With
public class Recipe {
    private final UUID id;
    private final String name;
    private final String description;
    private final String instructions;
    private final Integer preparationTime;
    private final String difficulty;
    private final BigDecimal cost;
    private final List<RecipeIngredient> recipeIngredients;

    public Recipe withCost(BigDecimal newCost) {
        return Recipe.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .instructions(this.instructions)
                .preparationTime(this.preparationTime)
                .difficulty(this.difficulty)
                .recipeIngredients(this.recipeIngredients)
                .cost(newCost)
                .build();
    }

    public Recipe withRecipeIngredients(List<RecipeIngredient> newIngredients) {
        return Recipe.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .instructions(this.instructions)
                .preparationTime(this.preparationTime)
                .difficulty(this.difficulty)
                .recipeIngredients(newIngredients)
                .cost(this.cost)
                .build();
    }
}
