package com.wilson.stock.infrastructure.api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateRecipeIngredientDto {
    private UUID ingredientId;
    private BigDecimal quantity;
    private String unit;
} 
