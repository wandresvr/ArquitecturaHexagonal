package com.itm.edu.stock.infrastructure.api.mapper;

import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeIngredientDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import com.itm.edu.stock.infrastructure.api.dto.RecipeResponseDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RecipeMapper {

    private final IngredientMapper ingredientMapper;

    public RecipeMapper(IngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    public RecipeResponseDto toDto(Recipe recipe) {
        RecipeResponseDto dto = new RecipeResponseDto();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setInstructions(recipe.getInstructions());
        dto.setPreparationTime(recipe.getPreparationTime());
        dto.setDifficulty(recipe.getDifficulty());
        dto.setIngredients(recipe.getIngredients().stream()
            .map(ingredientMapper::toDto)
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
        recipe.setIngredients(dto.getIngredients().stream()
            .map(this::toIngredient)
            .collect(Collectors.toList()));
        return recipe;
    }

    private Ingredient toIngredient(CreateRecipeIngredientDto dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(dto.getIngredientId());
        ingredient.setQuantity(dto.getQuantity());
        ingredient.setUnit(dto.getUnit());
        return ingredient;
    }
} 