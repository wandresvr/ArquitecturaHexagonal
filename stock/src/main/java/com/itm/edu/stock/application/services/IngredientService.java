package com.itm.edu.stock.application.services;

import com.itm.edu.stock.application.ports.input.CreateIngredientUseCase;
import com.itm.edu.stock.application.ports.input.GetIngredientUseCase;
import com.itm.edu.stock.application.ports.input.IngredientUseCase;
import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import com.itm.edu.stock.infrastructure.persistence.mapper.IngredientPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngredientService implements CreateIngredientUseCase, GetIngredientUseCase, IngredientUseCase {
    private final IngredientRepository ingredientRepository;
    private final IngredientPersistenceMapper ingredientMapper;

    @Override
    @Transactional
    public IngredientResponse createIngredient(CreateIngredientCommand command) {
        var dto = ingredientMapper.fromCommand(command);
        return ingredientRepository.save(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientResponse> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public IngredientResponse getIngredientById(UUID id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }

    @Override
    @Transactional
    public IngredientResponse updateIngredient(UUID id, CreateIngredientCommand command) {
        var existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));
        
        // Crear el DTO con los valores existentes y actualizar solo los campos proporcionados
        var dto = IngredientDto.builder()
                .id(id)
                .name(command.getName() != null ? command.getName() : existingIngredient.getName())
                .description(command.getDescription() != null ? command.getDescription() : existingIngredient.getDescription())
                .quantity(command.getQuantity() != null ? command.getQuantity() : existingIngredient.getQuantity())
                .unit(command.getUnit() != null ? command.getUnit() : existingIngredient.getUnit())
                .price(command.getPrice() != null ? command.getPrice() : existingIngredient.getPrice())
                .supplier(command.getSupplier() != null ? command.getSupplier() : existingIngredient.getSupplier())
                .minimumStock(command.getMinimumStock() != null ? command.getMinimumStock() : existingIngredient.getMinimumStock())
                .build();
                
        return ingredientRepository.save(dto);
    }

    @Override
    @Transactional
    public void deleteIngredient(UUID id) {
        ingredientRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientResponse> getIngredientsBySupplier(String supplier) {
        return ingredientRepository.findBySupplier(supplier);
    }

    @Override
    @Transactional
    public void updateIngredientQuantity(UUID id, BigDecimal quantity) {
        var ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente no encontrado"));
        
        var dto = IngredientDto.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .description(ingredient.getDescription())
                .quantity(quantity)
                .unit(ingredient.getUnit())
                .price(ingredient.getPrice())
                .supplier(ingredient.getSupplier())
                .minimumStock(ingredient.getMinimumStock())
                .build();
                
        ingredientRepository.save(dto);
    }
}