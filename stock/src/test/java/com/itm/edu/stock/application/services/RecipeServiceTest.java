package com.itm.edu.stock.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeDto;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeIngredientDto;
import com.itm.edu.stock.infrastructure.persistence.mapper.RecipeMapper;
import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientCommand;
import com.itm.edu.stock.application.dto.RecipeIngredientResponse;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.domain.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    private UUID testId;
    private UUID ingredientId;
    private RecipeDto testDto;
    private CreateRecipeCommand testCommand;
    private RecipeResponse testResponse;
    private List<RecipeIngredientCommand> testIngredients;
    private List<RecipeIngredientResponse> testIngredientResponses;
    private List<RecipeIngredientDto> testIngredientDtos;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();
        
        testIngredients = Arrays.asList(
            new RecipeIngredientCommand(
                ingredientId,
                new BigDecimal("500"),
                "gramos"
            )
        );

        testCommand = new CreateRecipeCommand(
            "Torta de Chocolate",
            "Torta esponjosa de chocolate",
            "1. Mezclar ingredientes\n2. Hornear por 30 minutos",
            30,
            "Medio",
            testIngredients
        );

        testIngredientDtos = Arrays.asList(
            RecipeIngredientDto.builder()
                .id(UUID.randomUUID())
                .recipeId(testId)
                .ingredientId(ingredientId)
                .ingredientName("Harina")
                .quantity(new BigDecimal("500"))
                .unit("gramos")
                .build()
        );

        testIngredientResponses = Arrays.asList(
            RecipeIngredientResponse.builder()
                .id(testIngredientDtos.get(0).getId())
                .recipeId(testId)
                .ingredientId(ingredientId)
                .ingredientName("Harina")
                .quantity(new BigDecimal("500"))
                .unit("gramos")
                .build()
        );

        testDto = RecipeDto.builder()
            .id(testId)
            .name(testCommand.getName())
            .description(testCommand.getDescription())
            .instructions(testCommand.getInstructions())
            .preparationTime(testCommand.getPreparationTime())
            .difficulty(testCommand.getDifficulty())
            .cost(BigDecimal.ZERO)
            .recipeIngredients(testIngredientDtos)
            .build();

        testResponse = RecipeResponse.builder()
            .id(testId)
            .name(testCommand.getName())
            .description(testCommand.getDescription())
            .instructions(testCommand.getInstructions())
            .preparationTime(testCommand.getPreparationTime())
            .difficulty(testCommand.getDifficulty())
            .cost(BigDecimal.ZERO)
            .ingredients(testIngredientResponses)
            .build();
    }

    @Test
    void testCreateRecipe() {
        // Arrange
        when(ingredientRepository.existsById(ingredientId)).thenReturn(true);
        when(recipeMapper.fromCommand(testCommand)).thenReturn(testDto);
        when(recipeRepository.save(testDto)).thenReturn(testResponse);

        // Act
        RecipeResponse result = recipeService.createRecipe(testCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        assertEquals(testResponse.getName(), result.getName());
        assertEquals(testResponse.getDescription(), result.getDescription());
        assertEquals(testResponse.getInstructions(), result.getInstructions());
        assertEquals(testResponse.getPreparationTime(), result.getPreparationTime());
        assertEquals(testResponse.getDifficulty(), result.getDifficulty());
        assertEquals(testResponse.getCost(), result.getCost());
        
        assertNotNull(result.getIngredients());
        assertEquals(testResponse.getIngredients().size(), result.getIngredients().size());
        
        RecipeIngredientResponse expectedIngredient = testResponse.getIngredients().get(0);
        RecipeIngredientResponse actualIngredient = result.getIngredients().get(0);
        assertEquals(expectedIngredient.getId(), actualIngredient.getId());
        assertEquals(expectedIngredient.getRecipeId(), actualIngredient.getRecipeId());
        assertEquals(expectedIngredient.getIngredientId(), actualIngredient.getIngredientId());
        assertEquals(expectedIngredient.getIngredientName(), actualIngredient.getIngredientName());
        assertEquals(expectedIngredient.getQuantity(), actualIngredient.getQuantity());
        assertEquals(expectedIngredient.getUnit(), actualIngredient.getUnit());
        
        verify(ingredientRepository).existsById(ingredientId);
        verify(recipeMapper).fromCommand(testCommand);
        verify(recipeRepository).save(testDto);
    }

    @Test
    void testCreateRecipeWithNonExistentIngredient() {
        // Arrange
        when(ingredientRepository.existsById(ingredientId)).thenReturn(false);

        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.createRecipe(testCommand));
        verify(ingredientRepository).existsById(ingredientId);
        verify(recipeMapper, never()).fromCommand(any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testCreateRecipeWithNullCommand() {
        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.createRecipe(null));
        verify(ingredientRepository, never()).existsById(any());
        verify(recipeMapper, never()).fromCommand(any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testCreateRecipeWithNullIngredients() {
        // Arrange
        CreateRecipeCommand commandWithNullIngredients = new CreateRecipeCommand(
            "Test Recipe",
            "Test Description",
            "Test Instructions",
            30,
            "Easy",
            null
        );

        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.createRecipe(commandWithNullIngredients));
        verify(ingredientRepository, never()).existsById(any());
        verify(recipeMapper, never()).fromCommand(any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testCreateRecipeWithEmptyIngredients() {
        // Arrange
        CreateRecipeCommand commandWithEmptyIngredients = new CreateRecipeCommand(
            "Test Recipe",
            "Test Description",
            "Test Instructions",
            30,
            "Easy",
            new ArrayList<>()
        );

        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.createRecipe(commandWithEmptyIngredients));
        verify(ingredientRepository, never()).existsById(any());
        verify(recipeMapper, never()).fromCommand(any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testGetAllRecipes() {
        // Arrange
        RecipeResponse response2 = RecipeResponse.builder()
            .id(UUID.randomUUID())
            .name("Pan Casero")
            .description("Pan casero tradicional")
            .instructions("1. Amasar\n2. Dejar leudar\n3. Hornear")
            .preparationTime(120)
            .difficulty("Fácil")
            .cost(BigDecimal.ZERO)
            .ingredients(new ArrayList<>())
            .build();

        List<RecipeResponse> responses = Arrays.asList(testResponse, response2);
        when(recipeRepository.findAll()).thenReturn(responses);

        // Act
        List<RecipeResponse> results = recipeService.getAllRecipes();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        
        // Verificar primera receta
        RecipeResponse firstResult = results.get(0);
        assertEquals(testResponse.getId(), firstResult.getId());
        assertEquals(testResponse.getName(), firstResult.getName());
        assertEquals(testResponse.getDescription(), firstResult.getDescription());
        assertEquals(testResponse.getInstructions(), firstResult.getInstructions());
        assertEquals(testResponse.getPreparationTime(), firstResult.getPreparationTime());
        assertEquals(testResponse.getDifficulty(), firstResult.getDifficulty());
        assertEquals(testResponse.getCost(), firstResult.getCost());
        assertEquals(testResponse.getIngredients().size(), firstResult.getIngredients().size());
        
        verify(recipeRepository).findAll();
    }

    @Test
    void testGetRecipeById() {
        // Arrange
        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));

        // Act
        RecipeResponse result = recipeService.getRecipeById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        assertEquals(testResponse.getName(), result.getName());
        assertEquals(testResponse.getDescription(), result.getDescription());
        assertEquals(testResponse.getInstructions(), result.getInstructions());
        assertEquals(testResponse.getPreparationTime(), result.getPreparationTime());
        assertEquals(testResponse.getDifficulty(), result.getDifficulty());
        assertEquals(testResponse.getCost(), result.getCost());
        assertEquals(testResponse.getIngredients().size(), result.getIngredients().size());
        
        verify(recipeRepository).findById(testId);
    }

    @Test
    void testGetRecipeById_NotFound() {
        // Arrange
        when(recipeRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.getRecipeById(testId));
        verify(recipeRepository).findById(testId);
    }

    @Test
    void testUpdateRecipe_Success() {
        // Arrange
        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.existsById(ingredientId)).thenReturn(true);
        when(recipeRepository.save(any(RecipeDto.class))).thenReturn(testResponse);

        // Act
        RecipeResponse result = recipeService.updateRecipe(testId, testCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        assertEquals(testResponse.getName(), result.getName());
        
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).existsById(ingredientId);
        verify(recipeRepository).save(any(RecipeDto.class));
    }

    @Test
    void testUpdateRecipe_NotFound() {
        // Arrange
        when(recipeRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.updateRecipe(testId, testCommand));
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository, never()).existsById(any());
        verify(recipeMapper, never()).fromCommand(any(), any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testUpdateRecipe_WithNullCommand() {
        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.updateRecipe(testId, null));
        verify(recipeRepository, never()).findById(any());
        verify(ingredientRepository, never()).existsById(any());
        verify(recipeMapper, never()).fromCommand(any(), any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testUpdateRecipe_WithInvalidIngredients() {
        // Arrange
        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.existsById(ingredientId)).thenReturn(false);

        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.updateRecipe(testId, testCommand));
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).existsById(ingredientId);
        verify(recipeMapper, never()).fromCommand(any(), any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testGetRecipesByDifficulty() {
        // Arrange
        String difficulty = "Medio";
        List<RecipeResponse> expectedRecipes = Arrays.asList(testResponse);
        when(recipeRepository.findByDifficulty(difficulty)).thenReturn(expectedRecipes);

        // Act
        List<RecipeResponse> result = recipeService.getRecipesByDifficulty(difficulty);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testResponse.getId(), result.get(0).getId());
        assertEquals(testResponse.getName(), result.get(0).getName());
        
        verify(recipeRepository).findByDifficulty(difficulty);
    }

    @Test
    void testDeleteRecipe_Success() {
        // Arrange
        when(recipeRepository.existsById(testId)).thenReturn(true);

        // Act
        recipeService.deleteRecipe(testId);

        // Assert
        verify(recipeRepository).existsById(testId);
        verify(recipeRepository).deleteById(testId);
    }

    @Test
    void testDeleteRecipe_NotFound() {
        // Arrange
        when(recipeRepository.existsById(testId)).thenReturn(false);

        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.deleteRecipe(testId));
        verify(recipeRepository).existsById(testId);
        verify(recipeRepository, never()).deleteById(any());
    }

    @Test
    void testCalculateRecipeCost_Success() {
        // Arrange
        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.findById(ingredientId))
            .thenReturn(Optional.of(IngredientResponse.builder()
                .id(ingredientId)
                .name("Harina")
                .price(new BigDecimal("2.50"))
                .build()));

        // Act
        BigDecimal cost = recipeService.calculateRecipeCost(testId);

        // Assert
        assertNotNull(cost);
        assertEquals(new BigDecimal("1250.00"), cost); // 500 * 2.50
        
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).findById(ingredientId);
    }

    @Test
    void testCalculateRecipeCost_RecipeNotFound() {
        // Arrange
        when(recipeRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.calculateRecipeCost(testId));
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository, never()).findById(any());
    }

    @Test
    void testCalculateRecipeCost_IngredientNotFound() {
        // Arrange
        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.calculateRecipeCost(testId));
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).findById(ingredientId);
    }

    @Test
    void testUpdateRecipe_WithPartialData() {
        // Arrange
        CreateRecipeCommand partialCommand = new CreateRecipeCommand(
            "Nuevo Nombre",
            null,  // description se mantiene
            null,  // instructions se mantienen
            null,  // preparationTime se mantiene
            null,  // difficulty se mantiene
            null   // ingredients se mantienen
        );

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(recipeRepository.save(any(RecipeDto.class))).thenReturn(testResponse);

        // Act
        RecipeResponse result = recipeService.updateRecipe(testId, partialCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        assertEquals(testResponse.getName(), result.getName());
        verify(recipeRepository).findById(testId);
        verify(recipeRepository).save(any(RecipeDto.class));
    }

    @Test
    void testCalculateRecipeCost_WithEmptyIngredients() {
        // Arrange
        RecipeResponse recipeWithNoIngredients = RecipeResponse.builder()
            .id(testId)
            .name("Test Recipe")
            .ingredients(new ArrayList<>())
            .build();

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(recipeWithNoIngredients));

        // Act
        BigDecimal cost = recipeService.calculateRecipeCost(testId);

        // Assert
        assertEquals(BigDecimal.ZERO, cost);
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository, never()).findById(any());
    }

    @Test
    void testCalculateRecipeCost_WithNullIngredients() {
        // Arrange
        RecipeResponse recipeWithNullIngredients = RecipeResponse.builder()
            .id(testId)
            .name("Test Recipe")
            .ingredients(null)
            .build();

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(recipeWithNullIngredients));

        // Act
        BigDecimal cost = recipeService.calculateRecipeCost(testId);

        // Assert
        assertEquals(BigDecimal.ZERO, cost);
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository, never()).findById(any());
    }

    @Test
    void testGetRecipesByDifficulty_WithEmptyResult() {
        // Arrange
        String difficulty = "Experto";
        when(recipeRepository.findByDifficulty(difficulty)).thenReturn(new ArrayList<>());

        // Act
        List<RecipeResponse> result = recipeService.getRecipesByDifficulty(difficulty);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(recipeRepository).findByDifficulty(difficulty);
    }

    @Test
    void testGetAllRecipes_WithEmptyResult() {
        // Arrange
        when(recipeRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<RecipeResponse> result = recipeService.getAllRecipes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(recipeRepository).findAll();
    }

    @Test
    void testUpdateRecipe_WithNewIngredients() {
        // Arrange
        UUID newIngredientId = UUID.randomUUID();
        List<RecipeIngredientCommand> newIngredients = Arrays.asList(
            new RecipeIngredientCommand(
                newIngredientId,
                new BigDecimal("300"),
                "gramos"
            )
        );

        CreateRecipeCommand updateCommand = new CreateRecipeCommand(
            testCommand.getName(),
            testCommand.getDescription(),
            testCommand.getInstructions(),
            testCommand.getPreparationTime(),
            testCommand.getDifficulty(),
            newIngredients
        );

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.existsById(newIngredientId)).thenReturn(true);
        when(recipeRepository.save(any(RecipeDto.class))).thenReturn(testResponse);

        // Act
        RecipeResponse result = recipeService.updateRecipe(testId, updateCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).existsById(newIngredientId);
        verify(recipeRepository).save(any(RecipeDto.class));
    }

    @Test
    void testUpdateRecipe_WithInvalidNewIngredient() {
        // Arrange
        UUID invalidIngredientId = UUID.randomUUID();
        List<RecipeIngredientCommand> newIngredients = Arrays.asList(
            new RecipeIngredientCommand(
                invalidIngredientId,
                new BigDecimal("300"),
                "gramos"
            )
        );

        CreateRecipeCommand updateCommand = new CreateRecipeCommand(
            testCommand.getName(),
            testCommand.getDescription(),
            testCommand.getInstructions(),
            testCommand.getPreparationTime(),
            testCommand.getDifficulty(),
            newIngredients
        );

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.existsById(invalidIngredientId)).thenReturn(false);

        // Act & Assert
        assertThrows(BusinessException.class, () -> recipeService.updateRecipe(testId, updateCommand));
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).existsById(invalidIngredientId);
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testUpdateRecipe_WithAllFieldsNull() {
        // Arrange
        CreateRecipeCommand emptyCommand = new CreateRecipeCommand(
            null, null, null, null, null, null
        );

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(recipeRepository.save(any(RecipeDto.class))).thenReturn(testResponse);

        // Act
        RecipeResponse result = recipeService.updateRecipe(testId, emptyCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getName(), result.getName());
        assertEquals(testResponse.getDescription(), result.getDescription());
        assertEquals(testResponse.getInstructions(), result.getInstructions());
        assertEquals(testResponse.getPreparationTime(), result.getPreparationTime());
        assertEquals(testResponse.getDifficulty(), result.getDifficulty());
        verify(recipeRepository).findById(testId);
        verify(recipeRepository).save(any(RecipeDto.class));
    }

    @Test
    void testUpdateRecipe_WithEmptyIngredientsList() {
        // Arrange
        CreateRecipeCommand commandWithEmptyIngredients = new CreateRecipeCommand(
            "Test Recipe",
            "Test Description",
            "Test Instructions",
            30,
            "Fácil",
            new ArrayList<>()
        );

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(recipeRepository.save(any(RecipeDto.class))).thenReturn(testResponse);

        // Act
        RecipeResponse result = recipeService.updateRecipe(testId, commandWithEmptyIngredients);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        verify(recipeRepository).findById(testId);
        verify(recipeRepository).save(any(RecipeDto.class));
    }

    @Test
    void testCalculateRecipeCost_WithMultipleIngredients() {
        // Arrange
        UUID ingredient1Id = UUID.randomUUID();
        UUID ingredient2Id = UUID.randomUUID();
        
        List<RecipeIngredientResponse> ingredients = Arrays.asList(
            RecipeIngredientResponse.builder()
                .id(UUID.randomUUID())
                .ingredientId(ingredient1Id)
                .quantity(new BigDecimal("500"))
                .unit("gramos")
                .build(),
            RecipeIngredientResponse.builder()
                .id(UUID.randomUUID())
                .ingredientId(ingredient2Id)
                .quantity(new BigDecimal("250"))
                .unit("gramos")
                .build()
        );

        RecipeResponse recipeWithMultipleIngredients = RecipeResponse.builder()
            .id(testId)
            .name("Test Recipe")
            .ingredients(ingredients)
            .build();

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(recipeWithMultipleIngredients));
        when(ingredientRepository.findById(ingredient1Id))
            .thenReturn(Optional.of(IngredientResponse.builder()
                .id(ingredient1Id)
                .name("Harina")
                .price(new BigDecimal("2.00"))
                .build()));
        when(ingredientRepository.findById(ingredient2Id))
            .thenReturn(Optional.of(IngredientResponse.builder()
                .id(ingredient2Id)
                .name("Azúcar")
                .price(new BigDecimal("3.00"))
                .build()));

        // Act
        BigDecimal cost = recipeService.calculateRecipeCost(testId);

        // Assert
        assertEquals(new BigDecimal("1750.00"), cost); // (500 * 2.00) + (250 * 3.00)
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).findById(ingredient1Id);
        verify(ingredientRepository).findById(ingredient2Id);
    }

    @Test
    void testCalculateRecipeCost_WithMissingIngredient() {
        // Arrange
        UUID existingIngredientId = UUID.randomUUID();
        UUID missingIngredientId = UUID.randomUUID();
        
        List<RecipeIngredientResponse> ingredients = Arrays.asList(
            RecipeIngredientResponse.builder()
                .id(UUID.randomUUID())
                .ingredientId(existingIngredientId)
                .quantity(new BigDecimal("500"))
                .unit("gramos")
                .build(),
            RecipeIngredientResponse.builder()
                .id(UUID.randomUUID())
                .ingredientId(missingIngredientId)
                .quantity(new BigDecimal("250"))
                .unit("gramos")
                .build()
        );

        RecipeResponse recipe = RecipeResponse.builder()
            .id(testId)
            .name("Test Recipe")
            .ingredients(ingredients)
            .build();

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(recipe));
        when(ingredientRepository.findById(existingIngredientId))
            .thenReturn(Optional.of(IngredientResponse.builder()
                .id(existingIngredientId)
                .name("Harina")
                .price(new BigDecimal("2.00"))
                .build()));
        when(ingredientRepository.findById(missingIngredientId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeService.calculateRecipeCost(testId));
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).findById(existingIngredientId);
        verify(ingredientRepository).findById(missingIngredientId);
    }

    @Test
    void testGetRecipesByDifficulty_WithMultipleResults() {
        // Arrange
        String difficulty = "Medio";
        RecipeResponse recipe2 = RecipeResponse.builder()
            .id(UUID.randomUUID())
            .name("Otra Receta")
            .difficulty(difficulty)
            .build();

        List<RecipeResponse> expectedRecipes = Arrays.asList(testResponse, recipe2);
        when(recipeRepository.findByDifficulty(difficulty)).thenReturn(expectedRecipes);

        // Act
        List<RecipeResponse> results = recipeService.getRecipesByDifficulty(difficulty);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(testResponse.getId(), results.get(0).getId());
        assertEquals(recipe2.getId(), results.get(1).getId());
        verify(recipeRepository).findByDifficulty(difficulty);
    }

    @Test
    void testUpdateRecipe_WithSameIngredients() {
        // Arrange
        CreateRecipeCommand updateCommand = new CreateRecipeCommand(
            "Updated Name",
            "Updated Description",
            testCommand.getInstructions(),
            testCommand.getPreparationTime(),
            testCommand.getDifficulty(),
            testCommand.getIngredients()
        );

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.existsById(ingredientId)).thenReturn(true);
        when(recipeRepository.save(any(RecipeDto.class))).thenReturn(testResponse);

        // Act
        RecipeResponse result = recipeService.updateRecipe(testId, updateCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).existsById(ingredientId);
        verify(recipeRepository).save(any(RecipeDto.class));
    }

    @Test
    void testUpdateRecipe_WithMultipleInvalidIngredients() {
        // Arrange
        UUID invalidIngredient1Id = UUID.randomUUID();
        UUID invalidIngredient2Id = UUID.randomUUID();
        List<RecipeIngredientCommand> invalidIngredients = Arrays.asList(
            new RecipeIngredientCommand(invalidIngredient1Id, new BigDecimal("300"), "gramos"),
            new RecipeIngredientCommand(invalidIngredient2Id, new BigDecimal("200"), "gramos")
        );

        CreateRecipeCommand updateCommand = new CreateRecipeCommand(
            testCommand.getName(),
            testCommand.getDescription(),
            testCommand.getInstructions(),
            testCommand.getPreparationTime(),
            testCommand.getDifficulty(),
            invalidIngredients
        );

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.existsById(any(UUID.class))).thenReturn(false);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> recipeService.updateRecipe(testId, updateCommand));
        assertTrue(exception.getMessage().contains(invalidIngredient1Id.toString()));
        assertTrue(exception.getMessage().contains(invalidIngredient2Id.toString()));
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository, times(2)).existsById(any(UUID.class));
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void testCalculateRecipeCost_WithZeroQuantity() {
        // Arrange
        List<RecipeIngredientResponse> ingredients = Arrays.asList(
            RecipeIngredientResponse.builder()
                .id(UUID.randomUUID())
                .ingredientId(ingredientId)
                .quantity(BigDecimal.ZERO)
                .unit("gramos")
                .build()
        );

        RecipeResponse recipe = RecipeResponse.builder()
            .id(testId)
            .name("Test Recipe")
            .ingredients(ingredients)
            .build();

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(recipe));
        when(ingredientRepository.findById(ingredientId))
            .thenReturn(Optional.of(IngredientResponse.builder()
                .id(ingredientId)
                .name("Harina")
                .price(new BigDecimal("2.00"))
                .build()));

        // Act
        BigDecimal cost = recipeService.calculateRecipeCost(testId);

        // Assert
        assertEquals(0, cost.compareTo(BigDecimal.ZERO), "El costo debería ser cero");
        verify(recipeRepository).findById(testId);
        verify(ingredientRepository).findById(ingredientId);
    }

    @Test
    void testUpdateRecipe_WithNullAndEmptyFields() {
        // Arrange
        CreateRecipeCommand updateCommand = new CreateRecipeCommand(
            "",  // nombre vacío
            null, // descripción nula
            "",  // instrucciones vacías
            null, // tiempo de preparación nulo
            "",  // dificultad vacía
            new ArrayList<>() // lista vacía de ingredientes
        );

        when(recipeRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(recipeRepository.save(any(RecipeDto.class))).thenReturn(testResponse);

        // Act
        RecipeResponse result = recipeService.updateRecipe(testId, updateCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        verify(recipeRepository).findById(testId);
        verify(recipeRepository).save(any(RecipeDto.class));
    }
} 