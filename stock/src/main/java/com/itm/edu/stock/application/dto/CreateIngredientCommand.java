package com.itm.edu.stock.application.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class CreateIngredientCommand {
    String name;
    String description;
    BigDecimal quantity;
    String unit;
    String supplier;
    BigDecimal minimumStock;
    BigDecimal price;
} 