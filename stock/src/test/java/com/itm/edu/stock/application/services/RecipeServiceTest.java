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
} 