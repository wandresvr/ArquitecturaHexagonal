package com.itm.edu.stock.application.services;

import com.itm.edu.stock.application.ports.input.RecipeUseCase;
import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.RecipeResponseDto;
import com.itm.edu.stock.infrastructure.api.mapper.RecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService implements RecipeUseCase {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    @Override
    public RecipeResponseDto createRecipe(CreateRecipeRequestDto request) {
        Recipe recipe = recipeMapper.toEntity(request);
        calculateRecipeCost(recipe);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return recipeMapper.toDto(savedRecipe);
    }

    @Override
    public List<RecipeResponseDto> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeResponseDto getRecipeById(UUID id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        return recipeMapper.toDto(recipe);
    }

    @Override
    public RecipeResponseDto updateRecipe(UUID id, CreateRecipeRequestDto request) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        
        Recipe updatedRecipe = recipeMapper.toEntity(request);
        existingRecipe.setName(updatedRecipe.getName());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        existingRecipe.setInstructions(updatedRecipe.getInstructions());
        existingRecipe.setPreparationTime(updatedRecipe.getPreparationTime());
        existingRecipe.setDifficulty(updatedRecipe.getDifficulty());
        existingRecipe.setRecipeIngredients(updatedRecipe.getRecipeIngredients());
        
        calculateRecipeCost(existingRecipe);
        Recipe savedRecipe = recipeRepository.save(existingRecipe);
        return recipeMapper.toDto(savedRecipe);
    }

    @Override
    public List<RecipeResponseDto> getRecipesByDifficulty(String difficulty) {
        return recipeRepository.findByDifficulty(difficulty).stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRecipe(UUID id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public BigDecimal calculateRecipeCost(UUID id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        calculateRecipeCost(recipe);
        return recipe.getCost();
    }

    private void calculateRecipeCost(Recipe recipe) {
        BigDecimal totalCost = recipe.getRecipeIngredients().stream()
                .map(ri -> ri.getIngredient().getPrice().multiply(ri.getQuantity().getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        recipe.setCost(totalCost);
    }
} 