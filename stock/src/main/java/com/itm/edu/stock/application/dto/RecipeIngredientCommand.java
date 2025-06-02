package com.itm.edu.stock.application.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.UUID;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RecipeIngredientCommand {
    UUID ingredientId;
    BigDecimal quantity;
    String unit;
} 