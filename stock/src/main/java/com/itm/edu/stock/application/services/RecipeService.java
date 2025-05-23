package com.itm.edu.stock.application.services;

import com.itm.edu.stock.application.ports.input.RecipeUseCase;
import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientResponse;
import com.itm.edu.stock.domain.exception.BusinessException;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeDto;
import com.itm.edu.stock.infrastructure.persistence.mapper.RecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService implements RecipeUseCase {
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;

    @Override
    @Transactional
    public RecipeResponse createRecipe(CreateRecipeCommand command) {
        // Validar que todos los ingredientes existan
        List<UUID> nonExistentIngredients = command.getIngredients().stream()
                .map(ingredient -> ingredient.getIngredientId())
                .filter(ingredientId -> !ingredientRepository.existsById(ingredientId))
                .collect(Collectors.toList());

        if (!nonExistentIngredients.isEmpty()) {
            throw new BusinessException("Los siguientes ingredientes no existen: " + 
                nonExistentIngredients.stream()
                    .map(UUID::toString)
                    .collect(Collectors.joining(", ")));
        }

        RecipeDto dto = recipeMapper.fromCommand(command);
        return recipeRepository.save(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeResponse getRecipeById(UUID id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Receta no encontrada"));
    }

    @Override
    @Transactional
    public RecipeResponse updateRecipe(UUID id, CreateRecipeCommand command) {
        if (!recipeRepository.existsById(id)) {
            throw new RuntimeException("Recipe not found");
        }
        
        // Validar que todos los ingredientes existan
        List<UUID> nonExistentIngredients = command.getIngredients().stream()
                .map(ingredient -> ingredient.getIngredientId())
                .filter(ingredientId -> !ingredientRepository.existsById(ingredientId))
                .collect(Collectors.toList());

        if (!nonExistentIngredients.isEmpty()) {
            throw new BusinessException("Los siguientes ingredientes no existen: " + 
                nonExistentIngredients.stream()
                    .map(UUID::toString)
                    .collect(Collectors.joining(", ")));
        }
        
        RecipeDto dto = recipeMapper.fromCommand(command);
        return recipeRepository.save(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponse> getRecipesByDifficulty(String difficulty) {
        return recipeRepository.findByDifficulty(difficulty);
    }

    @Override
    @Transactional
    public void deleteRecipe(UUID id) {
        if (!recipeRepository.existsById(id)) {
            throw new BusinessException("Receta no encontrada");
        }
        recipeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BigDecimal calculateRecipeCost(UUID id) {
        RecipeResponse recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        
        return calculateRecipeCost(recipe.getIngredients());
    }

    private BigDecimal calculateRecipeCost(List<RecipeIngredientResponse> ingredients) {
        return ingredients.stream()
                .map(ingredient -> {
                    var ingredientData = ingredientRepository.findById(ingredient.getIngredientId())
                            .orElseThrow(() -> new RuntimeException("Ingredient not found"));
                    return ingredientData.getPrice().multiply(ingredient.getQuantity());
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
} 