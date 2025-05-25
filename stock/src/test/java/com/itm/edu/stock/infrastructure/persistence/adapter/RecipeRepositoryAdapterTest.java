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
import java.util.ArrayList;

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

    private UUID recipeId;
    private UUID ingredientId;
    private RecipeDto recipeDto;
    private RecipeJpaEntity recipeEntity;
    private RecipeResponse recipeResponse;
    private RecipeIngredientDto testIngredientDto;
    private RecipeIngredientJpaEntity testIngredientEntity;
    private RecipeIngredientResponse testIngredientResponse;
    private IngredientJpaEntity testIngredientJpaEntity;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();

        testIngredientDto = RecipeIngredientDto.builder()
            .id(UUID.randomUUID())
            .recipeId(recipeId)
            .ingredientId(ingredientId)
            .ingredientName("Harina")
            .quantity(new BigDecimal("500"))
            .unit("gramos")
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

        testIngredientEntity = RecipeIngredientJpaEntity.builder()
            .id(testIngredientDto.getId())
            .recipe(recipeEntity)
            .ingredient(testIngredientJpaEntity)
            .quantity(testIngredientDto.getQuantity())
            .unit(testIngredientDto.getUnit())
            .build();

        recipeEntity = RecipeJpaEntity.builder()
            .id(recipeId)
            .name("Torta de Chocolate")
            .description("Torta esponjosa de chocolate")
            .instructions("1. Mezclar ingredientes\n2. Hornear por 30 minutos")
            .preparationTime(30)
            .difficulty("Medio")
            .cost(BigDecimal.valueOf(10.00))
            .recipeIngredients(Arrays.asList(testIngredientEntity))
            .build();

        testIngredientEntity.setRecipe(recipeEntity);

        testIngredientResponse = RecipeIngredientResponse.builder()
            .id(testIngredientDto.getId())
            .recipeId(recipeId)
            .ingredientId(testIngredientDto.getIngredientId())
            .ingredientName(testIngredientDto.getIngredientName())
            .quantity(testIngredientDto.getQuantity())
            .unit(testIngredientDto.getUnit())
            .build();

        recipeDto = RecipeDto.builder()
            .id(recipeId)
            .name("Torta de Chocolate")
            .description("Torta esponjosa de chocolate")
            .instructions("1. Mezclar ingredientes\n2. Hornear por 30 minutos")
            .preparationTime(30)
            .difficulty("Medio")
            .cost(BigDecimal.valueOf(10.00))
            .recipeIngredients(Arrays.asList(testIngredientDto))
            .build();

        recipeResponse = RecipeResponse.builder()
            .id(recipeId)
            .name("Torta de Chocolate")
            .description("Torta esponjosa de chocolate")
            .instructions("1. Mezclar ingredientes\n2. Hornear por 30 minutos")
            .preparationTime(30)
            .difficulty("Medio")
            .cost(BigDecimal.valueOf(10.00))
            .ingredients(Arrays.asList(testIngredientResponse))
            .build();
    }

    @Test
    void testSave_NewRecipe_Success() {
        // Arrange
        when(mapper.toEntity(recipeDto)).thenReturn(recipeEntity);
        when(jpaRepository.existsById(recipeId)).thenReturn(false);
        when(jpaRepository.save(recipeEntity)).thenReturn(recipeEntity);
        when(mapper.toDto(recipeEntity)).thenReturn(recipeDto);
        when(mapper.toResponse(recipeDto)).thenReturn(recipeResponse);

        // Act
        RecipeResponse result = adapter.save(recipeDto);

        // Assert
        assertNotNull(result);
        assertEquals(recipeResponse.getId(), result.getId());
        assertEquals(recipeResponse.getName(), result.getName());
        assertEquals(recipeResponse.getDescription(), result.getDescription());
        assertEquals(recipeResponse.getInstructions(), result.getInstructions());
        assertEquals(recipeResponse.getPreparationTime(), result.getPreparationTime());
        assertEquals(recipeResponse.getDifficulty(), result.getDifficulty());
        assertEquals(recipeResponse.getCost(), result.getCost());
        assertEquals(1, result.getIngredients().size());

        RecipeIngredientResponse ingredient = result.getIngredients().get(0);
        assertEquals(testIngredientDto.getId(), ingredient.getId());
        assertEquals(testIngredientDto.getIngredientId(), ingredient.getIngredientId());
        assertEquals(testIngredientDto.getIngredientName(), ingredient.getIngredientName());
        assertEquals(testIngredientDto.getQuantity(), ingredient.getQuantity());
        assertEquals(testIngredientDto.getUnit(), ingredient.getUnit());

        verify(mapper).toEntity(recipeDto);
        verify(jpaRepository).existsById(recipeId);
        verify(jpaRepository).save(recipeEntity);
        verify(mapper).toDto(recipeEntity);
        verify(mapper).toResponse(recipeDto);
        verify(jpaRepository, never()).findById(any());
    }

    @Test
    void testSave_ExistingRecipe_Success() {
        // Arrange
        when(mapper.toEntity(recipeDto)).thenReturn(recipeEntity);
        when(jpaRepository.existsById(recipeId)).thenReturn(true);
        when(jpaRepository.findById(recipeId)).thenReturn(Optional.of(recipeEntity));
        when(jpaRepository.save(recipeEntity)).thenReturn(recipeEntity);
        when(mapper.toDto(recipeEntity)).thenReturn(recipeDto);
        when(mapper.toResponse(recipeDto)).thenReturn(recipeResponse);

        // Act
        RecipeResponse result = adapter.save(recipeDto);

        // Assert
        assertNotNull(result);
        assertEquals(recipeResponse.getId(), result.getId());
        assertEquals(recipeResponse.getName(), result.getName());
        assertEquals(recipeResponse.getDescription(), result.getDescription());
        assertEquals(recipeResponse.getInstructions(), result.getInstructions());
        assertEquals(recipeResponse.getPreparationTime(), result.getPreparationTime());
        assertEquals(recipeResponse.getDifficulty(), result.getDifficulty());
        assertEquals(recipeResponse.getCost(), result.getCost());
        assertEquals(1, result.getIngredients().size());

        RecipeIngredientResponse ingredient = result.getIngredients().get(0);
        assertEquals(testIngredientDto.getId(), ingredient.getId());
        assertEquals(testIngredientDto.getIngredientId(), ingredient.getIngredientId());
        assertEquals(testIngredientDto.getIngredientName(), ingredient.getIngredientName());
        assertEquals(testIngredientDto.getQuantity(), ingredient.getQuantity());
        assertEquals(testIngredientDto.getUnit(), ingredient.getUnit());

        verify(mapper).toEntity(recipeDto);
        verify(jpaRepository).existsById(recipeId);
        verify(jpaRepository).findById(recipeId);
        verify(jpaRepository).save(recipeEntity);
        verify(mapper).toDto(recipeEntity);
        verify(mapper).toResponse(recipeDto);
    }

    @Test
    void testSave_ExistingRecipe_NotFound() {
        // Arrange
        when(mapper.toEntity(recipeDto)).thenReturn(recipeEntity);
        when(jpaRepository.existsById(recipeId)).thenReturn(true);
        when(jpaRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> adapter.save(recipeDto));
        
        verify(mapper).toEntity(recipeDto);
        verify(jpaRepository).existsById(recipeId);
        verify(jpaRepository).findById(recipeId);
        verify(jpaRepository, never()).save(any());
        verify(mapper, never()).toDto(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void testFindById_Success() {
        // Arrange
        when(jpaRepository.findById(recipeId)).thenReturn(Optional.of(recipeEntity));
        when(mapper.toDto(recipeEntity)).thenReturn(recipeDto);
        when(mapper.toResponse(recipeDto)).thenReturn(recipeResponse);

        // Act
        Optional<RecipeResponse> result = adapter.findById(recipeId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(recipeResponse.getId(), result.get().getId());
        assertEquals(recipeResponse.getName(), result.get().getName());
        assertEquals(recipeResponse.getDescription(), result.get().getDescription());
        assertEquals(recipeResponse.getInstructions(), result.get().getInstructions());
        assertEquals(recipeResponse.getPreparationTime(), result.get().getPreparationTime());
        assertEquals(recipeResponse.getDifficulty(), result.get().getDifficulty());
        assertEquals(recipeResponse.getCost(), result.get().getCost());
        assertEquals(1, result.get().getIngredients().size());

        RecipeIngredientResponse ingredient = result.get().getIngredients().get(0);
        assertEquals(testIngredientDto.getId(), ingredient.getId());
        assertEquals(testIngredientDto.getIngredientId(), ingredient.getIngredientId());
        assertEquals(testIngredientDto.getIngredientName(), ingredient.getIngredientName());
        assertEquals(testIngredientDto.getQuantity(), ingredient.getQuantity());
        assertEquals(testIngredientDto.getUnit(), ingredient.getUnit());

        verify(jpaRepository).findById(recipeId);
        verify(mapper).toDto(recipeEntity);
        verify(mapper).toResponse(recipeDto);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(jpaRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Act
        Optional<RecipeResponse> result = adapter.findById(recipeId);

        // Assert
        assertFalse(result.isPresent());
        verify(jpaRepository).findById(recipeId);
        verify(mapper, never()).toDto(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void testFindAll_Success() {
        // Arrange
        List<RecipeJpaEntity> entities = Arrays.asList(recipeEntity);
        when(jpaRepository.findAll()).thenReturn(entities);
        when(mapper.toDto(recipeEntity)).thenReturn(recipeDto);
        when(mapper.toResponse(recipeDto)).thenReturn(recipeResponse);

        // Act
        List<RecipeResponse> results = adapter.findAll();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(recipeResponse.getId(), results.get(0).getId());
        assertEquals(recipeResponse.getName(), results.get(0).getName());
        assertEquals(recipeResponse.getDescription(), results.get(0).getDescription());
        assertEquals(recipeResponse.getInstructions(), results.get(0).getInstructions());
        assertEquals(recipeResponse.getPreparationTime(), results.get(0).getPreparationTime());
        assertEquals(recipeResponse.getDifficulty(), results.get(0).getDifficulty());
        assertEquals(recipeResponse.getCost(), results.get(0).getCost());
        assertEquals(1, results.get(0).getIngredients().size());

        RecipeIngredientResponse ingredient = results.get(0).getIngredients().get(0);
        assertEquals(testIngredientDto.getId(), ingredient.getId());
        assertEquals(testIngredientDto.getIngredientId(), ingredient.getIngredientId());
        assertEquals(testIngredientDto.getIngredientName(), ingredient.getIngredientName());
        assertEquals(testIngredientDto.getQuantity(), ingredient.getQuantity());
        assertEquals(testIngredientDto.getUnit(), ingredient.getUnit());

        verify(jpaRepository).findAll();
        verify(mapper).toDto(recipeEntity);
        verify(mapper).toResponse(recipeDto);
    }

    @Test
    void testFindAll_EmptyList() {
        // Arrange
        when(jpaRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<RecipeResponse> results = adapter.findAll();

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        
        verify(jpaRepository).findAll();
        verify(mapper, never()).toDto(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void testDeleteById() {
        // Act
        adapter.deleteById(recipeId);

        // Assert
        verify(jpaRepository).deleteById(recipeId);
    }
} 