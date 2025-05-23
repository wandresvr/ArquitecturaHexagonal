package com.itm.edu.stock.infrastructure.api.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateIngredientRequestDto {
    private String name;
    private String description;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal price;
    private String supplier;
} 