package com.itm.edu.stock.infrastructure.persistence.repository;

import com.itm.edu.stock.infrastructure.persistence.entity.IngredientJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IngredientJpaRepository extends JpaRepository<IngredientJpaEntity, UUID> {
    List<IngredientJpaEntity> findBySupplier(String supplier);
} 