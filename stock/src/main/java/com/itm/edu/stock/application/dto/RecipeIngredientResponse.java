package com.itm.edu.stock.application.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class RecipeIngredientResponse {
    UUID id;
    UUID recipeId;
    UUID ingredientId;
    String ingredientName;
    BigDecimal quantity;
    String unit;
} 