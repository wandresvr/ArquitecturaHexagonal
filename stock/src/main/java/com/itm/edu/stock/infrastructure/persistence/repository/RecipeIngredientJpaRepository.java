package com.itm.edu.stock.infrastructure.persistence.repository;

import com.itm.edu.stock.infrastructure.persistence.entity.RecipeIngredientJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RecipeIngredientJpaRepository extends JpaRepository<RecipeIngredientJpaEntity, UUID> {
} 