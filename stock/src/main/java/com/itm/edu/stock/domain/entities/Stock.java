package com.itm.edu.stock.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private UUID id;
    private Ingredient ingredient;
    private Integer quantity;
    private String unit;
    private BigDecimal price;

    public void updateQuantity(Integer newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.quantity = newQuantity;
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.price = newPrice;
    }
} 