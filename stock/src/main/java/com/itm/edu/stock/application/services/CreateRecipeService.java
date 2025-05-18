package com.itm.edu.stock.application.services;

import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.repository.IngredientRepository;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.mapper.RecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;

    @Transactional
    public Recipe createRecipe(CreateRecipeRequestDto request) {
        Recipe recipe = recipeMapper.toEntity(request);
        recipe.setId(UUID.randomUUID());
        return recipeRepository.save(recipe);
    }
}