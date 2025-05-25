package com.itm.edu.stock.infrastructure.persistence.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class RecipeIngredientDto {
    UUID id;
    UUID recipeId;
    UUID ingredientId;
    String ingredientName;
    BigDecimal quantity;
    String unit;
} 