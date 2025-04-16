package com.wilson.stock.infrastructure.api.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class CreateRecipeRequestDto {
    @NotBlank(message = "El nombre de la receta es requerido")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String recipeName;

    private String description;

    @NotEmpty(message = "La receta debe tener al menos un ingrediente")
    @Size(min = 1, max = 20, message = "La receta debe tener entre 1 y 20 ingredientes")
    @Valid
    private List<CreateRecipeIngredientDto> ingredients;

    // Getters y Setters
    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<CreateRecipeIngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<CreateRecipeIngredientDto> ingredients) {
        this.ingredients = ingredients;
    }
} 