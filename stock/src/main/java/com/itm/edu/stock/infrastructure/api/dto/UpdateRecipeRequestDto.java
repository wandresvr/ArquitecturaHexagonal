package com.itm.edu.stock.infrastructure.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateRecipeRequestDto {
    private String name;
    private String description;
    private String instructions;
    private Integer preparationTime;
    private String difficulty;
    private List<CreateRecipeIngredientDto> ingredients;
} 