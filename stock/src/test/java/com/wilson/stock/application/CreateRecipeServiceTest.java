package com.wilson.stock.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.wilson.stock.application.services.CreateRecipeService;
import com.wilson.stock.domain.entities.Ingredient;
import com.wilson.stock.domain.entities.Recipe;
import com.wilson.stock.domain.entities.RecipeIngredient;
import com.wilson.stock.domain.repository.IngredientRepository;
import com.wilson.stock.domain.repository.RecipeRepository;
import com.wilson.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.wilson.stock.infrastructure.api.dto.CreateRecipeIngredientDto;

@ExtendWith(MockitoExtension.class)
class CreateRecipeServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private CreateRecipeService createRecipeService;

    @Test
    void whenCreateRecipe_thenSuccess() {
        // Given
        UUID ingredientId = UUID.randomUUID();
        CreateRecipeRequestDto requestDto = new CreateRecipeRequestDto();
        requestDto.setRecipeName("Pizza");
        
        CreateRecipeIngredientDto ingredientDto = new CreateRecipeIngredientDto();
        ingredientDto.setIngredientId(ingredientId);
        ingredientDto.setQuantity(new BigDecimal("500"));
        ingredientDto.setUnit("g");
        requestDto.setIngredients(Arrays.asList(ingredientDto));

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientId);
        ingredient.setName("Flour");

        Recipe expectedRecipe = new Recipe();
        expectedRecipe.setName("Pizza");

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe savedRecipe = invocation.getArgument(0);
            savedRecipe.setId(UUID.randomUUID());
            return savedRecipe;
        });

        // When
        Recipe result = createRecipeService.createRecipe(requestDto);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(expectedRecipe.getName(), result.getName());
        assertEquals(1, result.getIngredients().size());
        
        RecipeIngredient recipeIngredient = result.getIngredients().get(0);
        assertEquals(ingredientId, recipeIngredient.getIngredient().getId());
        assertEquals(new BigDecimal("500"), recipeIngredient.getQuantity().getValue());
        assertEquals("g", recipeIngredient.getUnit().getValue());
    }

    @Test
    void whenCreateRecipeWithNonExistentIngredient_thenThrowException() {
        // Given
        UUID ingredientId = UUID.randomUUID();
        CreateRecipeRequestDto requestDto = new CreateRecipeRequestDto();
        requestDto.setRecipeName("Pizza");
        
        CreateRecipeIngredientDto ingredientDto = new CreateRecipeIngredientDto();
        ingredientDto.setIngredientId(ingredientId);
        ingredientDto.setQuantity(new BigDecimal("500"));
        ingredientDto.setUnit("g");
        requestDto.setIngredients(Arrays.asList(ingredientDto));

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            createRecipeService.createRecipe(requestDto);
        });
    }
}