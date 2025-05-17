package com.itm.edu.stock.infrastructure.api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class IngredientResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal price;
    private String supplier;
} 