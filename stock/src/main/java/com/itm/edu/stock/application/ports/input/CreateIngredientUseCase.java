package com.itm.edu.stock.application.ports.input;

import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;
import java.util.UUID;

public interface CreateIngredientUseCase {
    IngredientResponse createIngredient(CreateIngredientCommand command);
    IngredientResponse updateIngredient(UUID id, CreateIngredientCommand command);
    void deleteIngredient(UUID id);
} 