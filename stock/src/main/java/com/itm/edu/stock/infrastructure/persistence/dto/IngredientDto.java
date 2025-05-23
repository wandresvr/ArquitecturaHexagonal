package com.itm.edu.stock.infrastructure.persistence.dto;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class IngredientDto {
    UUID id;
    String name;
    String description;
    BigDecimal quantity;
    String unit;
    BigDecimal price;
    String supplier;
    BigDecimal minimumStock;
} 