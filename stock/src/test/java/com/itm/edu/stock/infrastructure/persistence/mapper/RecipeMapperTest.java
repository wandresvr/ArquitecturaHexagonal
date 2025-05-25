package com.itm.edu.stock.infrastructure.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeDto;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeIngredientDto;
import com.itm.edu.stock.infrastructure.persistence.entity.RecipeJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.entity.RecipeIngredientJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.entity.IngredientJpaEntity;

@ExtendWith(MockitoExtension.class)
class RecipeMapperTest {

    @Mock
    private IngredientPersistenceMapper ingredientMapper;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeMapper recipeMapper;

    private UUID recipeId;
    private UUID ingredientId;
    private RecipeJpaEntity recipeEntity;
    private IngredientJpaEntity ingredientEntity;
    private RecipeIngredientJpaEntity recipeIngredientEntity;
    private CreateRecipeCommand createCommand;
    private RecipeDto recipeDto;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();

        ingredientEntity = IngredientJpaEntity.builder()
            .id(ingredientId)
            .name("Harina")
            .build();

        recipeIngredientEntity = RecipeIngredientJpaEntity.builder()
            .id(UUID.randomUUID())
            .ingredient(ingredientEntity)
            .quantity(new BigDecimal("500"))
            .unit("gramos")
            .build();

        recipeEntity = RecipeJpaEntity.builder()
            .id(recipeId)
            .name("Torta de Chocolate")
            .description("Torta esponjosa de chocolate")
            .instructions("1. Mezclar ingredientes\n2. Hornear")
            .preparationTime(30)
            .difficulty("Medio")
            .cost(new BigDecimal("10.00"))
            .recipeIngredients(Arrays.asList(recipeIngredientEntity))
            .build();

        recipeIngredientEntity.setRecipe(recipeEntity);

        createCommand = new CreateRecipeCommand(
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
    }

    @Test
    void testToDto_Success() {
        // Act
        RecipeDto result = recipeMapper.toDto(recipeEntity);

        // Assert
        assertNotNull(result);
        assertEquals(recipeEntity.getId(), result.getId());
        assertEquals(recipeEntity.getName(), result.getName());
        assertEquals(recipeEntity.getDescription(), result.getDescription());
        assertEquals(recipeEntity.getInstructions(), result.getInstructions());
        assertEquals(recipeEntity.getPreparationTime(), result.getPreparationTime());
        assertEquals(recipeEntity.getDifficulty(), result.getDifficulty());
        assertEquals(recipeEntity.getCost(), result.getCost());
        
        assertNotNull(result.getRecipeIngredients());
        assertEquals(1, result.getRecipeIngredients().size());
        
        RecipeIngredientDto ingredientDto = result.getRecipeIngredients().get(0);
        assertEquals(recipeIngredientEntity.getId(), ingredientDto.getId());
        assertEquals(recipeEntity.getId(), ingredientDto.getRecipeId());
        assertEquals(ingredientEntity.getId(), ingredientDto.getIngredientId());
        assertEquals(ingredientEntity.getName(), ingredientDto.getIngredientName());
        assertEquals(recipeIngredientEntity.getQuantity(), ingredientDto.getQuantity());
        assertEquals(recipeIngredientEntity.getUnit(), ingredientDto.getUnit());
    }

    @Test
    void testToDto_WithNullEntity() {
        // Act
        RecipeDto result = recipeMapper.toDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToDto_WithNullCost() {
        // Arrange
        recipeEntity.setCost(null);

        // Act
        RecipeDto result = recipeMapper.toDto(recipeEntity);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getCost());
    }

    @Test
    void testToResponse_Success() {
        // Act
        RecipeResponse result = recipeMapper.toResponse(recipeDto);

        // Assert
        assertNotNull(result);
        assertEquals(recipeDto.getId(), result.getId());
        assertEquals(recipeDto.getName(), result.getName());
        assertEquals(recipeDto.getDescription(), result.getDescription());
        assertEquals(recipeDto.getInstructions(), result.getInstructions());
        assertEquals(recipeDto.getPreparationTime(), result.getPreparationTime());
        assertEquals(recipeDto.getDifficulty(), result.getDifficulty());
        assertEquals(recipeDto.getCost(), result.getCost());
        
        assertNotNull(result.getIngredients());
        assertEquals(1, result.getIngredients().size());
    }

    @Test
    void testToResponse_WithNullDto() {
        // Act
        RecipeResponse result = recipeMapper.toResponse(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testToResponse_WithNullIngredients() {
        // Arrange
        RecipeDto dtoWithNullIngredients = recipeDto.toBuilder()
            .recipeIngredients(null)
            .build();

        // Act
        RecipeResponse result = recipeMapper.toResponse(dtoWithNullIngredients);

        // Assert
        assertNotNull(result);
        assertNull(result.getIngredients());
    }

    @Test
    void testFromCommand_Success() {
        // Act
        RecipeDto result = recipeMapper.fromCommand(createCommand);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId()); // Should generate a new UUID
        assertEquals(createCommand.getName(), result.getName());
        assertEquals(createCommand.getDescription(), result.getDescription());
        assertEquals(createCommand.getInstructions(), result.getInstructions());
        assertEquals(createCommand.getPreparationTime(), result.getPreparationTime());
        assertEquals(createCommand.getDifficulty(), result.getDifficulty());
        assertEquals(BigDecimal.ZERO, result.getCost());
        
        assertNotNull(result.getRecipeIngredients());
        assertEquals(1, result.getRecipeIngredients().size());
        
        RecipeIngredientDto ingredientDto = result.getRecipeIngredients().get(0);
        assertEquals(createCommand.getIngredients().get(0).getIngredientId(), ingredientDto.getIngredientId());
        assertEquals(createCommand.getIngredients().get(0).getQuantity(), ingredientDto.getQuantity());
        assertEquals(createCommand.getIngredients().get(0).getUnit(), ingredientDto.getUnit());
    }

    @Test
    void testFromCommand_WithNullCommand() {
        // Act
        RecipeDto result = recipeMapper.fromCommand((CreateRecipeCommand) null);

        // Assert
        assertNull(result);
    }

    @Test
    void testFromCommand_WithNullIngredients() {
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
        RecipeDto result = recipeMapper.fromCommand(commandWithNullIngredients);

        // Assert
        assertNotNull(result);
        assertNull(result.getRecipeIngredients());
    }

    @Test
    void testToEntity_Success() {
        // Arrange
        IngredientResponse ingredientResponse = IngredientResponse.builder()
            .id(ingredientId)
            .name("Harina")
            .unit("gramos")
            .build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredientResponse));
        when(ingredientMapper.fromResponse(ingredientResponse)).thenReturn(ingredientEntity);

        // Act
        RecipeJpaEntity result = recipeMapper.toEntity(recipeDto);

        // Assert
        assertNotNull(result);
        assertEquals(recipeDto.getId(), result.getId());
        assertEquals(recipeDto.getName(), result.getName());
        assertEquals(recipeDto.getDescription(), result.getDescription());
        assertEquals(recipeDto.getInstructions(), result.getInstructions());
        assertEquals(recipeDto.getPreparationTime(), result.getPreparationTime());
        assertEquals(recipeDto.getDifficulty(), result.getDifficulty());
        assertEquals(recipeDto.getCost(), result.getCost());
        
        assertNotNull(result.getRecipeIngredients());
        assertEquals(1, result.getRecipeIngredients().size());
        
        verify(ingredientRepository).findById(ingredientId);
        verify(ingredientMapper).fromResponse(ingredientResponse);
    }

    @Test
    void testToEntity_WithIngredientNotFound() {
        // Arrange
        when(ingredientRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> recipeMapper.toEntity(recipeDto));
        verify(ingredientRepository).findById(any());
        verify(ingredientMapper, never()).fromResponse(any());
    }
} 