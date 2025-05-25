package com.itm.edu.stock.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeDto;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeIngredientDto;
import com.itm.edu.stock.infrastructure.persistence.mapper.RecipeMapper;

@ExtendWith(MockitoExtension.class)
class CreateRecipeServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private CreateRecipeService createRecipeService;

    private UUID recipeId;
    private UUID ingredientId;
    private RecipeDto recipeDto;
    private RecipeResponse recipeResponse;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();

        recipeDto = RecipeDto.builder()
            .id(recipeId)
            .name("Torta de Chocolate")
            .description("Torta esponjosa de chocolate")
            .instructions("1. Mezclar ingredientes\n2. Hornear")
            .preparationTime(30)
            .difficulty("Medio")
            .cost(new BigDecimal("10.00"))
            .recipeIngredients(Arrays.asList(
                RecipeIngredientDto.builder()
                    .id(UUID.randomUUID())
                    .recipeId(recipeId)
                    .ingredientId(ingredientId)
                    .ingredientName("Harina")
                    .quantity(new BigDecimal("500"))
                    .unit("gramos")
                    .build()
            ))
            .build();

        recipeResponse = RecipeResponse.builder()
            .id(UUID.randomUUID())
            .name("Torta de Chocolate")
            .description("Torta esponjosa de chocolate")
            .instructions("1. Mezclar ingredientes\n2. Hornear")
            .preparationTime(30)
            .difficulty("Medio")
            .cost(new BigDecimal("10.00"))
            .build();
    }

    @Test
    void testCreateRecipe_Success() {
        // Arrange
        when(recipeRepository.save(any(RecipeDto.class))).thenReturn(recipeResponse);

        // Act
        RecipeResponse result = createRecipeService.createRecipe(recipeDto);

        // Assert
        assertNotNull(result);
        assertNotEquals(recipeId, result.getId()); // Should generate a new UUID
        assertEquals(recipeDto.getName(), result.getName());
        assertEquals(recipeDto.getDescription(), result.getDescription());
        assertEquals(recipeDto.getInstructions(), result.getInstructions());
        assertEquals(recipeDto.getPreparationTime(), result.getPreparationTime());
        assertEquals(recipeDto.getDifficulty(), result.getDifficulty());
        assertEquals(recipeDto.getCost(), result.getCost());
        
        verify(recipeRepository).save(any(RecipeDto.class));
    }

    @Test
    void testCreateRecipe_WithNullRecipe() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> createRecipeService.createRecipe(null));
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testCreateRecipe_WithExistingId() {
        // Arrange
        when(recipeRepository.save(any(RecipeDto.class))).thenReturn(recipeResponse);

        // Act
        RecipeResponse result = createRecipeService.createRecipe(recipeDto);

        // Assert
        assertNotNull(result);
        assertNotEquals(recipeId, result.getId()); // Should generate a new UUID
        
        verify(recipeRepository).save(any(RecipeDto.class));
    }
} 