package com.itm.edu.stock.application.dto;

import lombok.Value;
import java.util.UUID;
import java.math.BigDecimal;

@Value
public class RecipeIngredientCommand {
    UUID ingredientId;
    BigDecimal quantity;
    String unit;
} 