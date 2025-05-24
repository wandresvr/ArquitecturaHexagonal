package com.itm.edu.stock.domain.repository;

import com.itm.edu.stock.domain.entities.Ingredient;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngredientRepository {
    Ingredient save(Ingredient ingredient);
    Optional<Ingredient> findById(UUID id);
    List<Ingredient> findAll();
    void deleteById(UUID id);
    List<Ingredient> findBySupplier(String supplier);
}
