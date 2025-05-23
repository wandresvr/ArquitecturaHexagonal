package com.itm.edu.stock.infrastructure.persistence.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class RecipeDto {
    UUID id;
    String name;
    String description;
    String instructions;
    Integer preparationTime;
    String difficulty;
    BigDecimal cost;
    @Builder.Default
    List<RecipeIngredientDto> recipeIngredients = new ArrayList<>();
} 