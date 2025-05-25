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

    @Test
    void testToEntity_WithEmptyFields() {
        // Arrange
        CreateRecipeCommand emptyCommand = new CreateRecipeCommand(
            "",
            "",
            "",
            null,
            "",
            new ArrayList<>()
        );

        // Act
        Recipe result = mapper.toEntity(emptyCommand);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("", result.getName());
        assertEquals("", result.getDescription());
        assertEquals("", result.getInstructions());
        assertNull(result.getPreparationTime());
        assertEquals("", result.getDifficulty());
        assertEquals(BigDecimal.ZERO, result.getCost());
        assertNotNull(result.getRecipeIngredients());
        assertTrue(result.getRecipeIngredients().isEmpty());
    }

    @Test
    void testToEntity_WithMaximumValues() {
        // Arrange
        CreateRecipeCommand maxCommand = new CreateRecipeCommand(
            "Test Recipe With Very Long Name That Should Still Be Valid",
            "Test Description With Maximum Length That Should Be Accepted By The System And Still Process Correctly",
            "1. First instruction with maximum length\n2. Second instruction with maximum length\n3. Third instruction with maximum length",
            Integer.MAX_VALUE,
            "Very Difficult And Complex Recipe That Requires Expert Skills",
            Arrays.asList(
                new RecipeIngredientCommand(
                    ingredientId,
                    BigDecimal.valueOf(999999.99),
                    "kilogramos"
                ),
                new RecipeIngredientCommand(
                    UUID.randomUUID(),
                    BigDecimal.valueOf(999999.99),
                    "litros"
                )
            )
        );

        // Act
        Recipe result = mapper.toEntity(maxCommand);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(maxCommand.getName(), result.getName());
        assertEquals(maxCommand.getDescription(), result.getDescription());
        assertEquals(maxCommand.getInstructions(), result.getInstructions());
        assertEquals(maxCommand.getPreparationTime(), result.getPreparationTime());
        assertEquals(maxCommand.getDifficulty(), result.getDifficulty());
        assertEquals(BigDecimal.ZERO, result.getCost());
        assertNotNull(result.getRecipeIngredients());
        assertTrue(result.getRecipeIngredients().isEmpty());
    }

    @Test
    void testToResponse_WithNullRecipe() {
        // Act
        RecipeResponse result = mapper.toResponse(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToResponse_WithNullFields() {
        // Arrange
        Recipe nullFieldsRecipe = Recipe.builder()
            .id(recipeId)
            .name(null)
            .description(null)
            .instructions(null)
            .preparationTime(null)
            .difficulty(null)
            .cost(null)
            .recipeIngredients(null)
            .build();

        // Act
        RecipeResponse result = mapper.toResponse(nullFieldsRecipe);

        // Assert
        assertNotNull(result);
        assertEquals(recipeId, result.getId());
        assertNull(result.getName());
        assertNull(result.getDescription());
        assertNull(result.getInstructions());
        assertNull(result.getPreparationTime());
        assertNull(result.getDifficulty());
        assertNull(result.getCost());
        assertNotNull(result.getIngredients());
        assertTrue(result.getIngredients().isEmpty());
    }

    @Test
    void testToResponse_WithMultipleIngredients() {
        // Arrange
        RecipeIngredient secondIngredient = RecipeIngredient.builder()
            .id(UUID.randomUUID())
            .recipeId(recipeId)
            .ingredientId(UUID.randomUUID())
            .ingredientName("Azúcar")
            .quantity(new BigDecimal("250"))
            .unit("gramos")
            .build();

        Recipe recipeWithMultipleIngredients = recipe.toBuilder()
            .recipeIngredients(Arrays.asList(recipeIngredient, secondIngredient))
            .build();

        // Act
        RecipeResponse result = mapper.toResponse(recipeWithMultipleIngredients);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getIngredients());
        assertEquals(2, result.getIngredients().size());
        
        // Verificar primer ingrediente
        assertEquals(recipeIngredient.getId(), result.getIngredients().get(0).getId());
        assertEquals(recipeIngredient.getRecipeId(), result.getIngredients().get(0).getRecipeId());
        assertEquals(recipeIngredient.getIngredientId(), result.getIngredients().get(0).getIngredientId());
        assertEquals(recipeIngredient.getIngredientName(), result.getIngredients().get(0).getIngredientName());
        assertEquals(recipeIngredient.getQuantity(), result.getIngredients().get(0).getQuantity());
        assertEquals(recipeIngredient.getUnit(), result.getIngredients().get(0).getUnit());
        
        // Verificar segundo ingrediente
        assertEquals(secondIngredient.getId(), result.getIngredients().get(1).getId());
        assertEquals(secondIngredient.getRecipeId(), result.getIngredients().get(1).getRecipeId());
        assertEquals(secondIngredient.getIngredientId(), result.getIngredients().get(1).getIngredientId());
        assertEquals(secondIngredient.getIngredientName(), result.getIngredients().get(1).getIngredientName());
        assertEquals(secondIngredient.getQuantity(), result.getIngredients().get(1).getQuantity());
        assertEquals(secondIngredient.getUnit(), result.getIngredients().get(1).getUnit());
    }
} 