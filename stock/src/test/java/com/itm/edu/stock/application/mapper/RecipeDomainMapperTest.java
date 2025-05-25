package com.itm.edu.stock.application.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.entities.RecipeIngredient;
import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientCommand;
import com.itm.edu.stock.application.ports.output.IngredientRepository;

@ExtendWith(MockitoExtension.class)
class RecipeDomainMapperTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeDomainMapper mapper;

    private UUID recipeId;
    private UUID ingredientId;
    private CreateRecipeCommand command;
    private Recipe recipe;
    private RecipeIngredient recipeIngredient;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();

        command = new CreateRecipeCommand(
            "Torta de Chocolate",
            "Torta esponjosa de chocolate",
            "1. Mezclar ingredientes\n2. Hornear",
            30,
            "Medio",
            Arrays.asList(new RecipeIngredientCommand(
                ingredientId,
                new BigDecimal("500"),
                "gramos"
            ))
        );

        recipeIngredient = RecipeIngredient.builder()
            .id(UUID.randomUUID())
            .recipeId(recipeId)
            .ingredientId(ingredientId)
            .ingredientName("Harina")
            .quantity(new BigDecimal("500"))
            .unit("gramos")
            .build();

        recipe = Recipe.builder()
            .id(recipeId)
            .name("Torta de Chocolate")
            .description("Torta esponjosa de chocolate")
            .instructions("1. Mezclar ingredientes\n2. Hornear")
            .preparationTime(30)
            .difficulty("Medio")
            .cost(new BigDecimal("10.00"))
            .recipeIngredients(Arrays.asList(recipeIngredient))
            .build();
    }

    @Test
    void testToEntity_Success() {
        // Act
        Recipe result = mapper.toEntity(command);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId()); // Should generate a new UUID
        assertEquals(command.getName(), result.getName());
        assertEquals(command.getDescription(), result.getDescription());
        assertEquals(command.getInstructions(), result.getInstructions());
        assertEquals(command.getPreparationTime(), result.getPreparationTime());
        assertEquals(command.getDifficulty(), result.getDifficulty());
        assertEquals(BigDecimal.ZERO, result.getCost());
        assertNotNull(result.getRecipeIngredients());
        assertTrue(result.getRecipeIngredients().isEmpty());
    }

    @Test
    void testToResponse_Success() {
        // Act
        RecipeResponse result = mapper.toResponse(recipe);

        // Assert
        assertNotNull(result);
        assertEquals(recipe.getId(), result.getId());
        assertEquals(recipe.getName(), result.getName());
        assertEquals(recipe.getDescription(), result.getDescription());
        assertEquals(recipe.getInstructions(), result.getInstructions());
        assertEquals(recipe.getPreparationTime(), result.getPreparationTime());
        assertEquals(recipe.getDifficulty(), result.getDifficulty());
        assertEquals(recipe.getCost(), result.getCost());
        
        assertNotNull(result.getIngredients());
        assertEquals(1, result.getIngredients().size());
        
        assertEquals(recipeIngredient.getId(), result.getIngredients().get(0).getId());
        assertEquals(recipeIngredient.getRecipeId(), result.getIngredients().get(0).getRecipeId());
        assertEquals(recipeIngredient.getIngredientId(), result.getIngredients().get(0).getIngredientId());
        assertEquals(recipeIngredient.getIngredientName(), result.getIngredients().get(0).getIngredientName());
        assertEquals(recipeIngredient.getQuantity(), result.getIngredients().get(0).getQuantity());
        assertEquals(recipeIngredient.getUnit(), result.getIngredients().get(0).getUnit());
    }

    @Test
    void testToResponse_WithNullRecipeIngredients() {
        // Arrange
        Recipe recipeWithNullIngredients = recipe.toBuilder()
            .recipeIngredients(null)
            .build();

        // Act
        RecipeResponse result = mapper.toResponse(recipeWithNullIngredients);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }

    @Test
    void testToResponse_WithEmptyRecipeIngredients() {
        // Arrange
        Recipe recipeWithEmptyIngredients = recipe.toBuilder()
            .recipeIngredients(new ArrayList<>())
            .build();

        // Act
        RecipeResponse result = mapper.toResponse(recipeWithEmptyIngredients);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }

    @Test
    void testToEntity_WithNullCommand() {
        // Act
        Recipe result = mapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToEntity_WithNullIngredients() {
        // Arrange
        CreateRecipeCommand commandWithNullIngredients = new CreateRecipeCommand(
            "Test Recipe",
            "Test Description",
            "Test Instructions",
            30,
            "Easy",
            null
        );

        // Act
        Recipe result = mapper.toEntity(commandWithNullIngredients);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getRecipeIngredients());
        assertTrue(result.getRecipeIngredients().isEmpty());
    }
} 