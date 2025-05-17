package com.itm.edu.stock.application.ports.input;

import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IngredientUseCase {
    IngredientResponseDto createIngredient(CreateIngredientRequestDto request);
    IngredientResponseDto getIngredientById(UUID id);
    List<IngredientResponseDto> getAllIngredients();
    IngredientResponseDto updateIngredient(UUID id, CreateIngredientRequestDto request);
    void deleteIngredient(UUID id);
    List<IngredientResponseDto> getIngredientsBySupplier(String supplier);
    void updateIngredientQuantity(UUID id, BigDecimal quantity);
} 