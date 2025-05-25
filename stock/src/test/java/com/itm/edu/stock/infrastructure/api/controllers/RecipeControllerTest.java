package com.itm.edu.stock.infrastructure.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.edu.stock.application.ports.input.RecipeUseCase;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.RecipeResponseDto;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeIngredientDto;
import com.itm.edu.stock.infrastructure.api.mapper.RecipeApiMapper;
import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientResponse;
import com.itm.edu.stock.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeUseCase recipeUseCase;

    @MockBean
    private RecipeApiMapper recipeApiMapper;

    private CreateRecipeRequestDto validRequest;
    private RecipeResponse recipeResponse;
    private RecipeResponseDto responseDto;
    private UUID testId;
    private UUID ingredientId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();

        // Crear ingrediente de prueba
        CreateRecipeIngredientDto ingredientDto = new CreateRecipeIngredientDto();
        ingredientDto.setIngredientId(ingredientId);
        ingredientDto.setQuantity(new BigDecimal("500"));
        ingredientDto.setUnit("gramos");

        // Crear solicitud válida
        validRequest = new CreateRecipeRequestDto();
        validRequest.setName("Torta de Chocolate");
        validRequest.setDescription("Torta esponjosa de chocolate");
        validRequest.setInstructions("1. Mezclar ingredientes\n2. Hornear por 30 minutos");
        validRequest.setPreparationTime(30);
        validRequest.setDifficulty("Medio");
        validRequest.setIngredients(Arrays.asList(ingredientDto));

        // Crear respuesta de ingrediente
        RecipeIngredientResponse ingredientResponse = RecipeIngredientResponse.builder()
            .id(UUID.randomUUID())
            .recipeId(testId)
            .ingredientId(ingredientId)
            .ingredientName("Harina")
            .quantity(new BigDecimal("500"))
            .unit("gramos")
            .build();

        // Crear respuesta de receta
        recipeResponse = RecipeResponse.builder()
            .id(testId)
            .name(validRequest.getName())
            .description(validRequest.getDescription())
            .instructions(validRequest.getInstructions())
            .preparationTime(validRequest.getPreparationTime())
            .difficulty(validRequest.getDifficulty())
            .ingredients(Arrays.asList(ingredientResponse))
            .cost(BigDecimal.valueOf(10.00))
            .build();

        // Crear DTO de respuesta
        responseDto = new RecipeResponseDto();
        responseDto.setId(testId);
        responseDto.setName(validRequest.getName());
        responseDto.setDescription(validRequest.getDescription());
        responseDto.setInstructions(validRequest.getInstructions());
        responseDto.setPreparationTime(validRequest.getPreparationTime());
        responseDto.setDifficulty(validRequest.getDifficulty());
        responseDto.setCost(BigDecimal.valueOf(10.00));

        // Configurar comportamiento del mapper
        when(recipeApiMapper.toCommand(any(CreateRecipeRequestDto.class)))
            .thenReturn(new CreateRecipeCommand(
                validRequest.getName(),
                validRequest.getDescription(),
                validRequest.getInstructions(),
                validRequest.getPreparationTime(),
                validRequest.getDifficulty(),
                new ArrayList<>()
            ));
        when(recipeApiMapper.toDto(any(RecipeResponse.class))).thenReturn(responseDto);
    }

    @Test
    void createRecipe_Success() throws Exception {
        when(recipeUseCase.createRecipe(any())).thenReturn(recipeResponse);

        mockMvc.perform(post("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value(validRequest.getName()));

        verify(recipeUseCase).createRecipe(any());
    }

    @Test
    void createRecipe_InvalidRequest() throws Exception {
        validRequest.setName(null); // Hacer inválida la solicitud

        mockMvc.perform(post("/api/v1/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllRecipes_Success() throws Exception {
        when(recipeUseCase.getAllRecipes())
                .thenReturn(Arrays.asList(recipeResponse));

        mockMvc.perform(get("/api/v1/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testId.toString()))
                .andExpect(jsonPath("$[0].name").value(validRequest.getName()));

        verify(recipeUseCase).getAllRecipes();
    }

    @Test
    void getRecipeById_Success() throws Exception {
        when(recipeUseCase.getRecipeById(testId)).thenReturn(recipeResponse);

        mockMvc.perform(get("/api/v1/recipes/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value(validRequest.getName()));

        verify(recipeUseCase).getRecipeById(testId);
    }

    @Test
    void getRecipeById_NotFound() throws Exception {
        when(recipeUseCase.getRecipeById(testId))
                .thenThrow(new BusinessException("Recipe not found"));

        mockMvc.perform(get("/api/v1/recipes/{id}", testId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Recipe not found"));
    }

    @Test
    void updateRecipe_Success() throws Exception {
        when(recipeUseCase.updateRecipe(eq(testId), any()))
                .thenReturn(recipeResponse);

        mockMvc.perform(put("/api/v1/recipes/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value(validRequest.getName()));

        verify(recipeUseCase).updateRecipe(eq(testId), any());
    }

    @Test
    void deleteRecipe_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/recipes/{id}", testId))
                .andExpect(status().isNoContent());

        verify(recipeUseCase).deleteRecipe(testId);
    }

    @Test
    void getRecipesByDifficulty_Success() throws Exception {
        when(recipeUseCase.getRecipesByDifficulty("Medio"))
                .thenReturn(Arrays.asList(recipeResponse));

        mockMvc.perform(get("/api/v1/recipes/difficulty/{difficulty}", "Medio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testId.toString()))
                .andExpect(jsonPath("$[0].name").value(validRequest.getName()));

        verify(recipeUseCase).getRecipesByDifficulty("Medio");
    }
} 