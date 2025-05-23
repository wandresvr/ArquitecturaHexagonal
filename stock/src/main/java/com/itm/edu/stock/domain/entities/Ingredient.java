package com.itm.edu.stock.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.With;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@With
public class Ingredient {
    private final UUID id;
    private final String name;
    private final String description;
    private final BigDecimal quantity;
    private final String unit;
    private final BigDecimal price;
    private final String supplier;
    private final BigDecimal minimumStock;
    
    public boolean hasLowStock() {
        return quantity.compareTo(minimumStock) <= 0;
    }
    
    public Ingredient withQuantity(BigDecimal newQuantity) {
        return this.toBuilder()
                .quantity(newQuantity)
                .build();
    }
} 