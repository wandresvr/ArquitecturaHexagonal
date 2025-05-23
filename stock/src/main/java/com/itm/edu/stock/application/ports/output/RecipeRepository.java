package com.itm.edu.stock.application.ports.output;

import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository {
    RecipeResponse save(RecipeDto recipe);
    Optional<RecipeResponse> findById(UUID id);
    List<RecipeResponse> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
    List<RecipeResponse> findByDifficulty(String difficulty);
} 