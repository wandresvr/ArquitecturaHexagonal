package com.wilson.stock.application.ports.input;

import com.wilson.stock.domain.entities.Ingredient;
import com.wilson.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import java.util.List;
import java.util.UUID;

public interface IngredientUseCase {
    Ingredient createIngredient(CreateIngredientRequestDto request);
    List<Ingredient> getAllIngredients();
    Ingredient getIngredientById(UUID id);
} 