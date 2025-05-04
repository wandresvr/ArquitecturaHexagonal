package com.itm.edu.stock.application.ports.input;

import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.domain.entities.Recipe;

public interface CreateRecipeUseCase {
    Recipe createRecipe(CreateRecipeRequestDto request);
} 