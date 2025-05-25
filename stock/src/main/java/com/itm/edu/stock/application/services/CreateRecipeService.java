package com.itm.edu.stock.application.services;

import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeDto;
import com.itm.edu.stock.infrastructure.persistence.mapper.RecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateRecipeService {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeResponse createRecipe(RecipeDto recipe) {
        if (recipe == null) {
            throw new NullPointerException("La receta no puede ser nula");
        }
        
        // Generar un nuevo ID para la receta y sus ingredientes
        final UUID newId = generateUniqueId(recipe.getId());
        
        var updatedRecipe = recipe.toBuilder()
            .id(newId)
            .recipeIngredients(
                recipe.getRecipeIngredients() != null ?
                    recipe.getRecipeIngredients().stream()
                        .map(ri -> ri.toBuilder()
                            .id(UUID.randomUUID())
                            .recipeId(newId)
                            .build())
                        .collect(Collectors.toList()) :
                    new ArrayList<>()
            )
            .build();
            
        return recipeRepository.save(updatedRecipe);
    }
    
    private UUID generateUniqueId(UUID currentId) {
        UUID newId;
        do {
            newId = UUID.randomUUID();
        } while (currentId != null && currentId.equals(newId));
        return newId;
    }
}