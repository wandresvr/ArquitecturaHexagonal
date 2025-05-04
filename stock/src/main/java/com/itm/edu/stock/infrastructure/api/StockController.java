package com.itm.edu.stock.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

import com.itm.edu.stock.application.ports.input.CreateRecipeUseCase;
import com.itm.edu.stock.application.ports.input.IngredientUseCase;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {
    
    private final CreateRecipeUseCase createRecipeUseCase;
    private final IngredientUseCase ingredientUseCase;

    @PostMapping("/recipes")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody CreateRecipeRequestDto request) {
        Recipe recipe = createRecipeUseCase.createRecipe(request);
        return ResponseEntity.ok(recipe);
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Ingredient> createIngredient(@Valid @RequestBody CreateIngredientRequestDto request) {
        Ingredient ingredient = ingredientUseCase.createIngredient(request);
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredientUseCase.getAllIngredients());
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(ingredientUseCase.getIngredientById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 