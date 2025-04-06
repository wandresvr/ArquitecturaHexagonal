package com.wilson.stock.infrastructure.api.dto;

import lombok.Data;
import java.math.BigDecimal;
import com.wilson.stock.domain.valueobjects.Quantity;
import com.wilson.stock.domain.valueobjects.Unit;

@Data
public class CreateIngredientRequestDto {
    private String name;
    private String description;
    private BigDecimal quantity;
    private String unit;
} 