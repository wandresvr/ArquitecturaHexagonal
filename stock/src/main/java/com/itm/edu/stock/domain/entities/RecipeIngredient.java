package com.itm.edu.stock.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@With
public class RecipeIngredient {
    private UUID id;
    private UUID recipeId;
    private UUID ingredientId;
    private String ingredientName;
    private BigDecimal quantity;
    private String unit;

    public RecipeIngredient withQuantity(BigDecimal newQuantity) {
        return this.toBuilder()
                .quantity(newQuantity)
                .build();
    }

    public RecipeIngredient withUnit(String newUnit) {
        return this.toBuilder()
                .unit(newUnit)
                .build();
    }
} 