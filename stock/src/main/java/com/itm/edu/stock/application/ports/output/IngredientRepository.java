package com.itm.edu.stock.application.ports.output;

import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngredientRepository {
    IngredientResponse save(IngredientDto ingredient);
    Optional<IngredientResponse> findById(UUID id);
    List<IngredientResponse> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
    List<IngredientResponse> findBySupplier(String supplier);
} 