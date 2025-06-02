package com.itm.edu.stock.infrastructure.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
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