package com.itm.edu.stock.application.ports.input;

import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RecipeUseCase {
    RecipeResponse createRecipe(CreateRecipeCommand command);
    List<RecipeResponse> getAllRecipes();
    RecipeResponse getRecipeById(UUID id);
    RecipeResponse updateRecipe(UUID id, CreateRecipeCommand command);
    List<RecipeResponse> getRecipesByDifficulty(String difficulty);
    void deleteRecipe(UUID id);
    BigDecimal calculateRecipeCost(UUID id);
} 