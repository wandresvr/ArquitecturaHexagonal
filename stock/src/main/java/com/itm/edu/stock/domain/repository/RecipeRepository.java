package com.itm.edu.stock.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.itm.edu.stock.domain.entities.Recipe;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    Optional<Recipe> findByProductId(UUID productId);
}
