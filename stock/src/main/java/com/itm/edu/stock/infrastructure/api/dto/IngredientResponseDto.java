package com.itm.edu.stock.infrastructure.api.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class IngredientResponseDto {
    UUID id;
    String name;
    String description;
    BigDecimal quantity;
    String unit;
    String supplier;
    BigDecimal minimumStock;
} 