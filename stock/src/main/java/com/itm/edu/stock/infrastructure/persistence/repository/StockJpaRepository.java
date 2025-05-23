package com.itm.edu.stock.infrastructure.persistence.repository;

import com.itm.edu.stock.domain.entities.Stock;
import com.itm.edu.stock.infrastructure.persistence.base.BaseJpaRepository;
import com.itm.edu.stock.infrastructure.persistence.entity.StockJpaEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface StockJpaRepository extends BaseJpaRepository<StockJpaEntity, Stock> {
    List<StockJpaEntity> findByIngredientId(UUID ingredientId);
} 