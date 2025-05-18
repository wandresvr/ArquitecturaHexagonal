package com.itm.edu.stock.application.services;

import com.itm.edu.stock.application.ports.input.IngredientUseCase;
import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.domain.repository.IngredientRepository;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import com.itm.edu.stock.infrastructure.api.mapper.IngredientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService implements IngredientUseCase {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    @Override
    @Transactional
    public IngredientResponseDto createIngredient(CreateIngredientRequestDto request) {
        Ingredient ingredient = ingredientMapper.toEntity(request);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return ingredientMapper.toDto(savedIngredient);
    }

    @Override
    public List<IngredientResponseDto> getAllIngredients() {
        return ingredientRepository.findAll().stream()
            .map(ingredientMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public IngredientResponseDto getIngredientById(UUID id) {
        Ingredient ingredient = ingredientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));
        return ingredientMapper.toDto(ingredient);
    }

    @Override
    @Transactional
    public IngredientResponseDto updateIngredient(UUID id, CreateIngredientRequestDto request) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));

        Ingredient updatedIngredient = ingredientMapper.toEntity(request);
        updatedIngredient.setId(id);
        Ingredient savedIngredient = ingredientRepository.save(updatedIngredient);
        return ingredientMapper.toDto(savedIngredient);
    }

    @Override
    @Transactional
    public void deleteIngredient(UUID id) {
        ingredientRepository.deleteById(id);
    }

    @Override
    public List<IngredientResponseDto> getIngredientsBySupplier(String supplier) {
        return ingredientRepository.findBySupplier(supplier).stream()
            .map(ingredientMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateIngredientQuantity(UUID id, BigDecimal quantity) {
        Ingredient ingredient = ingredientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));
        ingredient.setQuantity(quantity);
        ingredientRepository.save(ingredient);
    }
}