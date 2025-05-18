package com.itm.edu.stock.application.ports.input;

import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.RecipeResponseDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RecipeUseCase {
    RecipeResponseDto createRecipe(CreateRecipeRequestDto request);
    List<RecipeResponseDto> getAllRecipes();
    RecipeResponseDto getRecipeById(UUID id);
    RecipeResponseDto updateRecipe(UUID id, CreateRecipeRequestDto request);
    List<RecipeResponseDto> getRecipesByDifficulty(String difficulty);
    void deleteRecipe(UUID id);
    BigDecimal calculateRecipeCost(UUID id);
} 