package com.itm.edu.stock.infrastructure.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class UpdateRecipeRequestDto {
    @NotBlank(message = "El nombre es requerido")
    private String name;
    
    private String description;
    
    @NotBlank(message = "Las instrucciones son requeridas")
    private String instructions;
    
    @NotNull(message = "El tiempo de preparación es requerido")
    @Min(value = 1, message = "El tiempo de preparación debe ser mayor a 0")
    private Integer preparationTime;
    
    @NotBlank(message = "La dificultad es requerida")
    private String difficulty;
    
    @NotEmpty(message = "La lista de ingredientes no puede estar vacía")
    private List<CreateRecipeIngredientDto> ingredients;
} 