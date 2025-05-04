package com.itm.edu.stock.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import com.itm.edu.stock.application.ports.input.CreateRecipeUseCase;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.repository.IngredientRepository;
import com.itm.edu.stock.domain.repository.RecipeRepository;

@Service
@RequiredArgsConstructor
public class CreateRecipeService implements CreateRecipeUseCase {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    @Override
    @Transactional
    public Recipe createRecipe(CreateRecipeRequestDto request) {
        Recipe recipe = new Recipe();
        recipe.setId(UUID.randomUUID());
        recipe.setName(request.getRecipeName());

        request.getIngredients().forEach(ingredientDto -> {
            Ingredient ingredient = ingredientRepository.findById(ingredientDto.getIngredientId())
                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + ingredientDto.getIngredientId()));
            recipe.addIngredient(ingredient, ingredientDto.getQuantity(), ingredientDto.getUnit());
        });

        return recipeRepository.save(recipe);
    }
}