package com.itm.edu.stock.infrastructure.api.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateIngredientRequestDto {
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    private String description;
    
    @NotNull(message = "La cantidad es requerida")
    @DecimalMin(value = "0.0", inclusive = true, message = "La cantidad debe ser mayor o igual a 0")
    private BigDecimal quantity;
    
    @NotBlank(message = "La unidad es requerida")
    private String unit;
    
    @NotNull(message = "El precio es requerido")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio debe ser mayor o igual a 0")
    private BigDecimal price;
    
    @NotBlank(message = "El proveedor es requerido")
    private String supplier;
} 