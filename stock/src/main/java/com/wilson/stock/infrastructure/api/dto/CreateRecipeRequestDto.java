package com.wilson.stock.infrastructure.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateRecipeRequestDto {
    private String recipeName;
    private List<CreateRecipeIngredientDto> ingredients;
} 