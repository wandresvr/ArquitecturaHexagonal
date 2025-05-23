package com.itm.edu.stock.domain.entities;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class RecipeIngredient {
    UUID id;
    UUID recipeId;
    UUID ingredientId;
    String ingredientName;
    BigDecimal quantity;
    String unit;

    public RecipeIngredient withQuantity(BigDecimal newQuantity) {
        return this.toBuilder().quantity(newQuantity).build();
    }

    public RecipeIngredient withUnit(String newUnit) {
        return this.toBuilder()
                .unit(newUnit)
                .build();
    }
} 