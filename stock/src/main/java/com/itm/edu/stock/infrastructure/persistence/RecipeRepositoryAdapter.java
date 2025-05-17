package com.itm.edu.stock.infrastructure.persistence;

import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.repository.RecipeRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RecipeRepositoryAdapter implements RecipeRepository {
    private final RecipeRepositoryJpa recipeRepositoryJpa;

    @Override
    public Recipe save(Recipe recipe) {
        return recipeRepositoryJpa.save(recipe);
    }

    @Override
    public Optional<Recipe> findById(UUID id) {
        return recipeRepositoryJpa.findById(id);
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepositoryJpa.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        recipeRepositoryJpa.deleteById(id);
    }

    @Override
    public List<Recipe> findByDifficulty(String difficulty) {
        return recipeRepositoryJpa.findByDifficulty(difficulty);
    }
} 