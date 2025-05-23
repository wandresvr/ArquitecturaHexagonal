package com.itm.edu.stock.infrastructure.persistence.repository;

import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.infrastructure.persistence.base.BaseJpaRepository;
import com.itm.edu.stock.infrastructure.persistence.entity.IngredientJpaEntity;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IngredientJpaRepository extends BaseJpaRepository<IngredientJpaEntity, Ingredient> {
    List<IngredientJpaEntity> findBySupplier(String supplier);
} 