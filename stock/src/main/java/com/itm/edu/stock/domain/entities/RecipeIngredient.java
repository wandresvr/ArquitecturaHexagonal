package com.itm.edu.stock.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.itm.edu.stock.domain.valueobjects.Quantity;
import com.itm.edu.stock.domain.valueobjects.Unit;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {
    private UUID id;
    private Recipe recipe;
    private Ingredient ingredient;
    private Quantity quantity;
    private Unit unit;

    public void updateQuantity(Quantity newQuantity) {
        if (newQuantity == null) {
            throw new IllegalArgumentException("La cantidad no puede ser nula");
        }
        this.quantity = newQuantity;
    }

    public void updateUnit(Unit newUnit) {
        if (newUnit == null) {
            throw new IllegalArgumentException("La unidad no puede ser nula");
        }
        this.unit = newUnit;
    }
} 