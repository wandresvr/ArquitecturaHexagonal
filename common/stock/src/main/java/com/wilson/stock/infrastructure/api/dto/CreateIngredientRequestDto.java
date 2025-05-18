package com.wilson.stock.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import com.wilson.stock.domain.valueobjects.Quantity;
import com.wilson.stock.domain.valueobjects.Unit;

@Getter
@Setter
public class CreateIngredientRequestDto {
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    private String description;
    
    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser positiva")
    private BigDecimal quantity;
    
    @NotBlank(message = "La unidad es requerida")
    private String unit;
} 