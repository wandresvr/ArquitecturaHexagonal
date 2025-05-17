package com.itm.edu.stock.application.ports.output;

import com.itm.edu.stock.domain.entities.Recipe;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipePersistencePort {
    Recipe save(Recipe recipe);
    Optional<Recipe> findById(UUID id);
    List<Recipe> findAll();
    void deleteById(UUID id);
    List<Recipe> findByDifficulty(String difficulty);
} 