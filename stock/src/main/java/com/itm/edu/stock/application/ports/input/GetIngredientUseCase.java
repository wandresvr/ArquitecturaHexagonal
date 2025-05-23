package com.itm.edu.stock.application.ports.input;

import com.itm.edu.stock.application.dto.IngredientResponse;
import java.util.List;
import java.util.UUID;

public interface GetIngredientUseCase {
    List<IngredientResponse> getAllIngredients();
    IngredientResponse getIngredientById(UUID id);
    List<IngredientResponse> getIngredientsBySupplier(String supplier);
} 