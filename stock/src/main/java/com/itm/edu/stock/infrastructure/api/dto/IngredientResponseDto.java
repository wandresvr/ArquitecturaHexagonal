package com.itm.edu.stock.infrastructure.api.dto;

import lombok.Value;
import java.math.BigDecimal;
import java.util.UUID;

@Value
public class IngredientResponseDto {
    UUID id;
    String name;
    String description;
    BigDecimal quantity;
    String unit;
    String supplier;
    BigDecimal minimumStock;
} 