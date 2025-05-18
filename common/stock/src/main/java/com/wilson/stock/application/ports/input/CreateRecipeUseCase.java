package com.wilson.stock.application.ports.input;

import com.wilson.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.wilson.stock.domain.entities.Recipe;

public interface CreateRecipeUseCase {
    Recipe createRecipe(CreateRecipeRequestDto request);
} 