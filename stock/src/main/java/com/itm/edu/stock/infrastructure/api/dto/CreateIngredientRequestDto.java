package com.itm.edu.stock.infrastructure.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateIngredientRequestDto {
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    private String description;
    
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 0, message = "La cantidad debe ser mayor o igual a 0")
    private BigDecimal quantity;
    
    @NotBlank(message = "La unidad es requerida")
    private String unit;
    
    @NotNull(message = "El precio es requerido")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private BigDecimal price;
    
    @NotBlank(message = "El proveedor es requerido")
    private String supplier;
} 