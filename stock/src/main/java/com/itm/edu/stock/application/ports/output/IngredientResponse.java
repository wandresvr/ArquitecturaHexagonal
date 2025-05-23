package com.itm.edu.stock.application.ports.output;

import com.itm.edu.stock.domain.valueobjects.Quantity;
import com.itm.edu.stock.domain.valueobjects.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class IngredientResponse {
    private final UUID id;
    private final String name;
    private final String description;
    private final Quantity quantity;
    private final Unit unit;
    private final BigDecimal price;
    private final String supplier;
} 