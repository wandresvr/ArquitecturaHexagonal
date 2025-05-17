package com.itm.edu.stock.infrastructure.api.mapper;

import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.entities.RecipeIngredient;
import com.itm.edu.stock.domain.repository.IngredientRepository;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeIngredientDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import com.itm.edu.stock.infrastructure.api.dto.RecipeResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Component
public class RecipeMapper {

    private final IngredientMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;

    public RecipeMapper(IngredientMapper ingredientMapper, IngredientRepository ingredientRepository) {
        this.ingredientMapper = ingredientMapper;
        this.ingredientRepository = ingredientRepository;
    }

    public RecipeResponseDto toDto(Recipe recipe) {
        RecipeResponseDto dto = new RecipeResponseDto();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setInstructions(recipe.getInstructions());
        dto.setPreparationTime(recipe.getPreparationTime());
        dto.setDifficulty(recipe.getDifficulty());
        dto.setIngredients(recipe.getRecipeIngredients().stream()
            .map(ri -> ingredientMapper.toDto(ri.getIngredient()))
            .collect(Collectors.toList()));
        dto.setCost(recipe.getCost());
        return dto;
    }

    public Recipe toEntity(CreateRecipeRequestDto dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setInstructions(dto.getInstructions());
        recipe.setPreparationTime(dto.getPreparationTime());
        recipe.setDifficulty(dto.getDifficulty());
        // Crear la lista de RecipeIngredient
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (CreateRecipeIngredientDto ingredientDto : dto.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(ingredientDto.getIngredientId())
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado: " + ingredientDto.getIngredientId()));
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setIngredient(ingredient);
            // Asignar cantidad y unidad si existen en el DTO
            if (ingredientDto.getQuantity() != null) {
                // Si tienes un value object Quantity, adáptalo aquí
                recipeIngredient.setQuantity(new com.itm.edu.stock.domain.valueobjects.Quantity(ingredientDto.getQuantity()));
            }
            if (ingredientDto.getUnit() != null) {
                recipeIngredient.setUnit(new com.itm.edu.stock.domain.valueobjects.Unit(ingredientDto.getUnit()));
            }
            recipeIngredients.add(recipeIngredient);
        }
        recipe.setRecipeIngredients(recipeIngredients);
        // Relación bidireccional
        for (RecipeIngredient ri : recipeIngredients) {
            ri.setRecipe(recipe);
        }
        return recipe;
    }
} 