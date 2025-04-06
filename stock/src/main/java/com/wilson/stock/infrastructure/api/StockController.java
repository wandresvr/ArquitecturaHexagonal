package com.wilson.stock.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

import com.wilson.stock.application.ports.input.CreateRecipeUseCase;
import com.wilson.stock.application.ports.input.IngredientUseCase;
import com.wilson.stock.domain.entities.Recipe;
import com.wilson.stock.domain.entities.Ingredient;
import com.wilson.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.wilson.stock.infrastructure.api.dto.CreateIngredientRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {
    
    private final CreateRecipeUseCase createRecipeUseCase;
    private final IngredientUseCase ingredientUseCase;

    @PostMapping("/recipes")
    public ResponseEntity<Recipe> createRecipe(@RequestBody CreateRecipeRequestDto request) {
        Recipe recipe = createRecipeUseCase.createRecipe(request);
        return ResponseEntity.ok(recipe);
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Ingredient> createIngredient(@RequestBody CreateIngredientRequestDto request) {
        Ingredient ingredient = ingredientUseCase.createIngredient(request);
        return ResponseEntity.ok(ingredient);
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredientUseCase.getAllIngredients());
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable UUID id) {
        return ResponseEntity.ok(ingredientUseCase.getIngredientById(id));
    }
} 