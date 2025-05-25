package com.itm.edu.stock.infrastructure.persistence.adapter;

import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeDto;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeIngredientDto;
import com.itm.edu.stock.infrastructure.persistence.entity.RecipeJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.entity.RecipeIngredientJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.entity.IngredientJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.repository.RecipeJpaRepository;
import com.itm.edu.stock.infrastructure.persistence.mapper.RecipeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeRepositoryAdapterTest {

    @Mock
    private RecipeJpaRepository jpaRepository;

    @Mock
    private RecipeMapper mapper;

    @InjectMocks
    private RecipeRepositoryAdapter adapter;

    private UUID testId;
    private UUID ingredientId;
    private RecipeDto testDto;
    private RecipeJpaEntity testEntity;
    private RecipeResponse testResponse;
    private RecipeIngredientDto testIngredientDto;
    private RecipeIngredientJpaEntity testIngredientEntity;
    private RecipeIngredientResponse testIngredientResponse;
    private IngredientJpaEntity testIngredientJpaEntity;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();

        testIngredientDto = RecipeIngredientDto.builder()
            .id(UUID.randomUUID())
            .recipeId(testId)
            .ingredientId(ingredientId)
            .ingredientName("Harina")
            .quantity(new BigDecimal("500"))
            .unit("gramos")
            .build();

        testDto = RecipeDto.builder()
            .id(testId)
            .name("Torta de Chocolate")
            .description("Torta esponjosa de chocolate")
            .instructions("1. Mezclar ingredientes\n2. Hornear por 30 minutos")
            .preparationTime(30)
            .difficulty("Medio")
            .cost(BigDecimal.valueOf(10.00))
            .recipeIngredients(Arrays.asList(testIngredientDto))
            .build();

        testIngredientJpaEntity = IngredientJpaEntity.builder()
            .id(ingredientId)
            .name("Harina")
            .description("Harina de trigo")
            .quantity(new BigDecimal("1000"))
            .unit("gramos")
            .price(new BigDecimal("2.50"))
            .supplier("Proveedor A")
            .minimumStock(BigDecimal.ZERO)
            .build();

        testEntity = RecipeJpaEntity.builder()
            .id(testId)
            .name(testDto.getName())
            .description(testDto.getDescription())
            .instructions(testDto.getInstructions())
            .preparationTime(testDto.getPreparationTime())
            .difficulty(testDto.getDifficulty())
            .cost(testDto.getCost())
            .build();

        testIngredientEntity = RecipeIngredientJpaEntity.builder()
            .id(testIngredientDto.getId())
            .recipe(testEntity)
            .ingredient(testIngredientJpaEntity)
            .quantity(testIngredientDto.getQuantity())
            .unit(testIngredientDto.getUnit())
            .build();

        testEntity.setRecipeIngredients(Arrays.asList(testIngredientEntity));

        testIngredientResponse = RecipeIngredientResponse.builder()
            .id(testIngredientDto.getId())
            .recipeId(testId)
            .ingredientId(testIngredientDto.getIngredientId())
            .ingredientName(testIngredientDto.getIngredientName())
            .quantity(testIngredientDto.getQuantity())
            .unit(testIngredientDto.getUnit())
            .build();

        testResponse = RecipeResponse.builder()
            .id(testId)
            .name(testDto.getName())
            .description(testDto.getDescription())
            .instructions(testDto.getInstructions())
            .preparationTime(testDto.getPreparationTime())
            .difficulty(testDto.getDifficulty())
            .cost(testDto.getCost())
            .ingredients(Arrays.asList(testIngredientResponse))
            .build();
    }

    @Test
    void save_Success() {
        // Arrange
        when(mapper.toEntity(testDto)).thenReturn(testEntity);
        when(jpaRepository.save(testEntity)).thenReturn(testEntity);
        when(mapper.toDto(testEntity)).thenReturn(testDto);
        when(mapper.toResponse(testDto)).thenReturn(testResponse);

        // Act
        RecipeResponse result = adapter.save(testDto);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testDto.getName(), result.getName());
        assertEquals(testDto.getDescription(), result.getDescription());
        assertEquals(testDto.getInstructions(), result.getInstructions());
        assertEquals(testDto.getPreparationTime(), result.getPreparationTime());
        assertEquals(testDto.getDifficulty(), result.getDifficulty());
        assertEquals(testDto.getCost(), result.getCost());
        assertEquals(1, result.getIngredients().size());

        RecipeIngredientResponse ingredient = result.getIngredients().get(0);
        assertEquals(testIngredientDto.getId(), ingredient.getId());
        assertEquals(testIngredientDto.getIngredientId(), ingredient.getIngredientId());
        assertEquals(testIngredientDto.getIngredientName(), ingredient.getIngredientName());
        assertEquals(testIngredientDto.getQuantity(), ingredient.getQuantity());
        assertEquals(testIngredientDto.getUnit(), ingredient.getUnit());

        verify(mapper).toEntity(testDto);
        verify(jpaRepository).save(testEntity);
        verify(mapper).toDto(testEntity);
        verify(mapper).toResponse(testDto);
    }

    @Test
    void findById_Success() {
        // Arrange
        when(jpaRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(mapper.toDto(testEntity)).thenReturn(testDto);
        when(mapper.toResponse(testDto)).thenReturn(testResponse);

        // Act
        Optional<RecipeResponse> result = adapter.findById(testId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testId, result.get().getId());
        assertEquals(testDto.getName(), result.get().getName());
        assertEquals(1, result.get().getIngredients().size());

        verify(jpaRepository).findById(testId);
        verify(mapper).toDto(testEntity);
        verify(mapper).toResponse(testDto);
    }

    @Test
    void findById_NotFound() {
        // Arrange
        when(jpaRepository.findById(testId)).thenReturn(Optional.empty());

        // Act
        Optional<RecipeResponse> result = adapter.findById(testId);

        // Assert
        assertFalse(result.isPresent());
        verify(jpaRepository).findById(testId);
        verify(mapper, never()).toDto(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void findAll_Success() {
        // Arrange
        List<RecipeJpaEntity> entities = Arrays.asList(testEntity);
        when(jpaRepository.findAll()).thenReturn(entities);
        when(mapper.toDto(testEntity)).thenReturn(testDto);
        when(mapper.toResponse(testDto)).thenReturn(testResponse);

        // Act
        List<RecipeResponse> results = adapter.findAll();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testId, results.get(0).getId());
        assertEquals(testDto.getName(), results.get(0).getName());
        assertEquals(1, results.get(0).getIngredients().size());

        verify(jpaRepository).findAll();
        verify(mapper).toDto(testEntity);
        verify(mapper).toResponse(testDto);
    }

    @Test
    void deleteById_Success() {
        // Act
        adapter.deleteById(testId);

        // Assert
        verify(jpaRepository).deleteById(testId);
    }

    @Test
    void save_WithExistingRecipe() {
        // Arrange
        when(jpaRepository.existsById(testId)).thenReturn(true);
        when(jpaRepository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(mapper.toEntity(testDto)).thenReturn(testEntity);
        when(jpaRepository.save(testEntity)).thenReturn(testEntity);
        when(mapper.toDto(testEntity)).thenReturn(testDto);
        when(mapper.toResponse(testDto)).thenReturn(testResponse);

        // Act
        RecipeResponse result = adapter.save(testDto);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testDto.getName(), result.getName());
        assertEquals(1, result.getIngredients().size());

        verify(jpaRepository).existsById(testId);
        verify(jpaRepository).findById(testId);
        verify(mapper).toEntity(testDto);
        verify(jpaRepository).save(testEntity);
        verify(mapper).toDto(testEntity);
        verify(mapper).toResponse(testDto);
    }
} 