package com.itm.edu.stock.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itm.edu.stock.domain.entities.Ingredient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    Ingredient save(Ingredient ingredient);
    Optional<Ingredient> findById(UUID id);
    List<Ingredient> findAll();
    void deleteById(UUID id);
    List<Ingredient> findBySupplier(String supplier);
}
