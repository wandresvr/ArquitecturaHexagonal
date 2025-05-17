package com.itm.edu.stock.application;

import com.itm.edu.stock.application.ports.input.RecipeUseCase;
import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.RecipeResponseDto;
import com.itm.edu.stock.infrastructure.api.mapper.RecipeMapper;
import com.itm.edu.stock.application.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    private CreateRecipeRequestDto requestDto;
    private Recipe recipe;
    private Recipe savedRecipe;
    private RecipeResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // Setup test data
        requestDto = new CreateRecipeRequestDto();
        requestDto.setName("Test Recipe");
        requestDto.setDescription("Test Description");
        requestDto.setInstructions("Test Instructions");
        requestDto.setPreparationTime(30);
        requestDto.setDifficulty("Easy");

        recipe = new Recipe();
        recipe.setId(UUID.randomUUID());
        recipe.setName(requestDto.getName());
        recipe.setDescription(requestDto.getDescription());
        recipe.setInstructions(requestDto.getInstructions());
        recipe.setPreparationTime(requestDto.getPreparationTime());
        recipe.setDifficulty(requestDto.getDifficulty());
        recipe.setRecipeIngredients(new ArrayList<>());

        savedRecipe = new Recipe();
        savedRecipe.setId(recipe.getId());
        savedRecipe.setName(recipe.getName());
        savedRecipe.setDescription(recipe.getDescription());
        savedRecipe.setInstructions(recipe.getInstructions());
        savedRecipe.setPreparationTime(recipe.getPreparationTime());
        savedRecipe.setDifficulty(recipe.getDifficulty());
        savedRecipe.setRecipeIngredients(new ArrayList<>());

        responseDto = new RecipeResponseDto();
        responseDto.setId(savedRecipe.getId());
        responseDto.setName(savedRecipe.getName());
        responseDto.setDescription(savedRecipe.getDescription());
        responseDto.setInstructions(savedRecipe.getInstructions());
        responseDto.setPreparationTime(savedRecipe.getPreparationTime());
        responseDto.setDifficulty(savedRecipe.getDifficulty());
    }

    @Test
    void testCreateRecipe() {
        // Arrange
        when(recipeMapper.toEntity(any(CreateRecipeRequestDto.class))).thenReturn(recipe);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);
        when(recipeMapper.toDto(any())).thenReturn(responseDto);

        // Act
        RecipeResponseDto result = recipeService.createRecipe(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getId(), result.getId());
        assertEquals(responseDto.getName(), result.getName());
        assertEquals(responseDto.getDescription(), result.getDescription());
        assertEquals(responseDto.getInstructions(), result.getInstructions());
        assertEquals(responseDto.getPreparationTime(), result.getPreparationTime());
        assertEquals(responseDto.getDifficulty(), result.getDifficulty());
        verify(recipeRepository).save(any());
    }
}