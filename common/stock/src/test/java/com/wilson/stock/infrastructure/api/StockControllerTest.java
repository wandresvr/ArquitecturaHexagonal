package com.wilson.stock.infrastructure.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilson.stock.application.ports.input.CreateRecipeUseCase;
import com.wilson.stock.application.ports.input.IngredientUseCase;
import com.wilson.stock.domain.entities.Ingredient;
import com.wilson.stock.domain.entities.Recipe;
import com.wilson.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.wilson.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.wilson.stock.infrastructure.api.dto.CreateRecipeIngredientDto;
import com.wilson.stock.domain.valueobjects.Quantity;
import com.wilson.stock.domain.valueobjects.Unit;

@WebMvcTest(StockController.class)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateRecipeUseCase createRecipeUseCase;

    @MockBean
    private IngredientUseCase ingredientUseCase;

    private Ingredient testIngredient;
    private CreateIngredientRequestDto createIngredientRequest;
    private CreateRecipeRequestDto createRecipeRequest;

    @BeforeEach
    void setUp() {
        // Preparar ingrediente de prueba
        testIngredient = new Ingredient();
        testIngredient.setId(UUID.randomUUID());
        testIngredient.setName("Harina");
        testIngredient.setDescription("Harina de trigo");
        testIngredient.setQuantity(new Quantity(new BigDecimal("1000")));
        testIngredient.setUnit(new Unit("gramos"));

        // Preparar DTO para crear ingrediente
        createIngredientRequest = new CreateIngredientRequestDto();
        createIngredientRequest.setName("Harina");
        createIngredientRequest.setDescription("Harina de trigo");
        createIngredientRequest.setQuantity(new BigDecimal("1000"));
        createIngredientRequest.setUnit("gramos");

        // Preparar DTO para crear receta
        CreateRecipeIngredientDto recipeIngredient = new CreateRecipeIngredientDto();
        recipeIngredient.setIngredientId(testIngredient.getId());
        recipeIngredient.setQuantity(new BigDecimal("500"));
        recipeIngredient.setUnit("gramos");

        createRecipeRequest = new CreateRecipeRequestDto();
        createRecipeRequest.setRecipeName("Pan");
        createRecipeRequest.setIngredients(Arrays.asList(recipeIngredient));
    }

    @Test
    void createIngredient_WithValidData_ShouldReturnCreatedIngredient() throws Exception {
        when(ingredientUseCase.createIngredient(any(CreateIngredientRequestDto.class)))
            .thenReturn(testIngredient);

        mockMvc.perform(post("/api/stock/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createIngredientRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Harina"))
                .andExpect(jsonPath("$.description").value("Harina de trigo"))
                .andExpect(jsonPath("$.quantity.value").value(1000))
                .andExpect(jsonPath("$.unit.value").value("gramos"));

        verify(ingredientUseCase).createIngredient(any(CreateIngredientRequestDto.class));
    }

    @Test
    void createIngredient_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateIngredientRequestDto invalidRequest = new CreateIngredientRequestDto();
        // No establecemos ningún campo, lo que debería ser inválido

        mockMvc.perform(post("/api/stock/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(ingredientUseCase, never()).createIngredient(any(CreateIngredientRequestDto.class));
    }

    @Test
    void createIngredient_WithNegativeQuantity_ShouldReturnBadRequest() throws Exception {
        createIngredientRequest.setQuantity(new BigDecimal("-1000"));

        mockMvc.perform(post("/api/stock/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createIngredientRequest)))
                .andExpect(status().isBadRequest());

        verify(ingredientUseCase, never()).createIngredient(any(CreateIngredientRequestDto.class));
    }

    @Test
    void getAllIngredients_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        when(ingredientUseCase.getAllIngredients()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/stock/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(ingredientUseCase).getAllIngredients();
    }

    @Test
    void getAllIngredients_WithMultipleIngredients_ShouldReturnAll() throws Exception {
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(UUID.randomUUID());
        ingredient2.setName("Azúcar");
        ingredient2.setQuantity(new Quantity(new BigDecimal("500")));
        ingredient2.setUnit(new Unit("gramos"));

        List<Ingredient> ingredients = Arrays.asList(testIngredient, ingredient2);
        when(ingredientUseCase.getAllIngredients()).thenReturn(ingredients);

        mockMvc.perform(get("/api/stock/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harina"))
                .andExpect(jsonPath("$[1].name").value("Azúcar"));

        verify(ingredientUseCase).getAllIngredients();
    }

    @Test
    void getIngredientById_WhenExists_ShouldReturnIngredient() throws Exception {
        when(ingredientUseCase.getIngredientById(testIngredient.getId()))
            .thenReturn(testIngredient);

        mockMvc.perform(get("/api/stock/ingredients/" + testIngredient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harina"))
                .andExpect(jsonPath("$.description").value("Harina de trigo"));

        verify(ingredientUseCase).getIngredientById(testIngredient.getId());
    }

    @Test
    void getIngredientById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        when(ingredientUseCase.getIngredientById(nonExistentId))
            .thenThrow(new RuntimeException("Ingredient not found"));

        mockMvc.perform(get("/api/stock/ingredients/" + nonExistentId))
                .andExpect(status().isNotFound());

        verify(ingredientUseCase).getIngredientById(nonExistentId);
    }

    @Test
    void createRecipe_WithValidData_ShouldReturnCreatedRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(UUID.randomUUID());
        recipe.setName("Pan");
        
        when(createRecipeUseCase.createRecipe(any(CreateRecipeRequestDto.class)))
            .thenReturn(recipe);

        mockMvc.perform(post("/api/stock/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRecipeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Pan"));

        verify(createRecipeUseCase).createRecipe(any(CreateRecipeRequestDto.class));
    }

    @Test
    void createRecipe_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateRecipeRequestDto invalidRequest = new CreateRecipeRequestDto();
        // No establecemos ningún campo, lo que debería ser inválido

        mockMvc.perform(post("/api/stock/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(createRecipeUseCase, never()).createRecipe(any(CreateRecipeRequestDto.class));
    }

    @Test
    void createRecipe_WithNonExistentIngredient_ShouldReturnBadRequest() throws Exception {
        CreateRecipeIngredientDto recipeIngredient = new CreateRecipeIngredientDto();
        recipeIngredient.setIngredientId(UUID.randomUUID()); // ID que no existe
        recipeIngredient.setQuantity(new BigDecimal("500"));
        recipeIngredient.setUnit("gramos");

        createRecipeRequest.setIngredients(Arrays.asList(recipeIngredient));

        when(createRecipeUseCase.createRecipe(any(CreateRecipeRequestDto.class)))
            .thenThrow(new RuntimeException("Ingredient not found"));

        mockMvc.perform(post("/api/stock/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRecipeRequest)))
                .andExpect(status().isBadRequest());

        verify(createRecipeUseCase).createRecipe(any(CreateRecipeRequestDto.class));
    }
} 