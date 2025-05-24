package com.itm.edu.stock.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@With
public class Recipe {
    private UUID id;
    private String name;
    private String description;
    private String instructions;
    private Integer preparationTime;
    private String difficulty;
    private BigDecimal cost;
    private List<RecipeIngredient> recipeIngredients;

    public Recipe withCost(BigDecimal newCost) {
        return this.toBuilder()
                .cost(newCost)
                .build();
    }

    public Recipe withRecipeIngredients(List<RecipeIngredient> newIngredients) {
        return this.toBuilder()
                .recipeIngredients(newIngredients)
                .build();
    }
}
