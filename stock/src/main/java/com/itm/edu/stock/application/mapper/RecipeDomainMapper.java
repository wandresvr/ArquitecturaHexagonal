package com.itm.edu.stock.application.mapper;

import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.entities.RecipeIngredient;
import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientResponse;
import com.itm.edu.stock.application.ports.output.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class RecipeDomainMapper {
    private final IngredientRepository ingredientRepository;
    
    public Recipe toEntity(CreateRecipeCommand command) {
        if (command == null) {
            return null;
        }
        return Recipe.builder()
                .id(UUID.randomUUID())
                .name(command.getName())
                .description(command.getDescription())
                .instructions(command.getInstructions())
                .preparationTime(command.getPreparationTime())
                .difficulty(command.getDifficulty())
                .recipeIngredients(new ArrayList<>())
                .cost(BigDecimal.ZERO)
                .build();
    }

    public RecipeResponse toResponse(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        return RecipeResponse.builder()
            .id(recipe.getId())
            .name(recipe.getName())
            .description(recipe.getDescription())
            .instructions(recipe.getInstructions())
            .preparationTime(recipe.getPreparationTime())
            .difficulty(recipe.getDifficulty())
            .cost(recipe.getCost())
            .ingredients(
                recipe.getRecipeIngredients() != null ?
                    recipe.getRecipeIngredients().stream()
                        .map(this::toIngredientResponse)
                        .collect(Collectors.toList()) :
                    new ArrayList<>()
            )
            .build();
    }

    private RecipeIngredientResponse toIngredientResponse(RecipeIngredient ri) {
        if (ri == null) {
            return null;
        }
        return RecipeIngredientResponse.builder()
            .id(ri.getId())
            .recipeId(ri.getRecipeId())
            .ingredientId(ri.getIngredientId())
            .ingredientName(ri.getIngredientName())
            .quantity(ri.getQuantity())
            .unit(ri.getUnit())
            .build();
    }
} 