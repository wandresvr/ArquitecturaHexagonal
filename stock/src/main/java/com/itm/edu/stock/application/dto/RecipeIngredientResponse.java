package com.itm.edu.stock.application.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class RecipeIngredientResponse {
    UUID id;
    UUID recipeId;
    UUID ingredientId;
    String ingredientName;
    BigDecimal quantity;
    String unit;
} 