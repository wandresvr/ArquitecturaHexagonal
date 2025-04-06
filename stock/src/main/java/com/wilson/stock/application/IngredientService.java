package com.wilson.stock.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

import com.wilson.stock.application.ports.input.IngredientUseCase;
import com.wilson.stock.domain.entities.Ingredient;
import com.wilson.stock.domain.repository.IngredientRepository;
import com.wilson.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.wilson.stock.domain.valueobjects.Quantity;
import com.wilson.stock.domain.valueobjects.Unit;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientService implements IngredientUseCase {
    
    private final IngredientRepository ingredientRepository;

    @Override
    @Transactional
    public Ingredient createIngredient(CreateIngredientRequestDto request) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());
        ingredient.setName(request.getName());
        ingredient.setDescription(request.getDescription());
        ingredient.setQuantity(new Quantity(request.getQuantity()));
        ingredient.setUnit(new Unit(request.getUnit()));
        
        return ingredientRepository.save(ingredient);
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient getIngredientById(UUID id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + id));
    }
} 