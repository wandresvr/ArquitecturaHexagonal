package com.itm.edu.stock.domain.repository;

import com.itm.edu.stock.domain.entities.Stock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository {
    Stock save(Stock stock);
    Optional<Stock> findById(UUID id);
    List<Stock> findAll();
    void deleteById(UUID id);
    List<Stock> findByIngredientId(UUID ingredientId);
} 