package com.itm.edu.stock.infrastructure.api.dto;

import com.itm.edu.stock.domain.entities.Ingredient;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class RecipeResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String instructions;
    private Integer preparationTime;
    private String difficulty;
    private List<IngredientResponseDto> ingredients;
    private BigDecimal cost;
} 