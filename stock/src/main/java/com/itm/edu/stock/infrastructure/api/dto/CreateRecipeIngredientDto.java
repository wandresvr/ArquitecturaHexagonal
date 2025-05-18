package com.itm.edu.stock.infrastructure.api.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateRecipeIngredientDto {
    @NotNull(message = "El ID del ingrediente es requerido")
    private UUID ingredientId;

    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private BigDecimal quantity;

    @NotNull(message = "La unidad es requerida")
    private String unit;

    // Getters y Setters
    public UUID getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(UUID ingredientId) {
        this.ingredientId = ingredientId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
} 
