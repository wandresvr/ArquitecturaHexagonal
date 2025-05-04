package com.itm.edu.stock.application.ports.input;

import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import java.util.List;
import java.util.UUID;

public interface IngredientUseCase {
    Ingredient createIngredient(CreateIngredientRequestDto request);
    List<Ingredient> getAllIngredients();
    Ingredient getIngredientById(UUID id);
} 