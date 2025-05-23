package com.itm.edu.stock.application.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class RecipeResponse {
    UUID id;
    String name;
    String description;
    String instructions;
    Integer preparationTime;
    String difficulty;
    BigDecimal cost;
    List<RecipeIngredientResponse> ingredients;
} 