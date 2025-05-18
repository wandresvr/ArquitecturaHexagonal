package com.itm.edu.stock.domain.repository;

import com.itm.edu.stock.domain.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepositoryJpa extends JpaRepository<Recipe, UUID> {
    Optional<Recipe> findById(UUID id);
    List<Recipe> findAll();
    List<Recipe> findByDifficulty(String difficulty);
    void deleteById(UUID id);
}
