package com.wilson.stock.application;

//import com.wilson.order.domain.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.wilson.stock.application.ports.input.CreateRecipeUseCase;
import com.wilson.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.wilson.stock.infrastructure.api.dto.CreateRecipeIngredientDto;
import com.wilson.stock.domain.entities.Ingredient;
import com.wilson.stock.domain.entities.Recipe;
import com.wilson.stock.domain.repository.IngredientRepository;
import com.wilson.stock.domain.repository.RecipeRepository;
import com.wilson.stock.domain.valueobjects.Quantity;
import com.wilson.stock.domain.valueobjects.Unit;

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