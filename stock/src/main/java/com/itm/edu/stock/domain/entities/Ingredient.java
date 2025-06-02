package com.itm.edu.stock.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Ingredient {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal price;
    private String supplier;
    private BigDecimal minimumStock;
    
    public boolean hasLowStock() {
        return quantity.compareTo(minimumStock) <= 0;
    }
    
    public Ingredient withQuantity(BigDecimal newQuantity) {
        return this.toBuilder()
                .quantity(newQuantity)
                .build();
    }
} 