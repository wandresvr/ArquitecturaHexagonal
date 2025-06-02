package com.itm.edu.stock.application.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IngredientResponse {
    UUID id;
    String name;
    String description;
    BigDecimal quantity;
    String unit;
    String supplier;
    BigDecimal minimumStock;
    BigDecimal price;
} 