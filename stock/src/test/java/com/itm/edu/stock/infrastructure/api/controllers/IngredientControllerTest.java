package com.itm.edu.stock.infrastructure.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.edu.stock.application.ports.input.CreateIngredientUseCase;
import com.itm.edu.stock.application.ports.input.GetIngredientUseCase;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import com.itm.edu.stock.infrastructure.api.mapper.IngredientApiMapper;
import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateIngredientUseCase createIngredientUseCase;

    @MockBean
    private GetIngredientUseCase getIngredientUseCase;

    @MockBean
    private IngredientApiMapper ingredientMapper;

    private CreateIngredientRequestDto validRequest;
    private IngredientResponse ingredientResponse;
    private IngredientResponseDto responseDto;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        
        validRequest = new CreateIngredientRequestDto();
        validRequest.setName("Harina");
        validRequest.setDescription("Harina de trigo");
        validRequest.setQuantity(new BigDecimal("1000"));
        validRequest.setUnit("gramos");
        validRequest.setPrice(new BigDecimal("2.50"));
        validRequest.setSupplier("Proveedor A");

        CreateIngredientCommand command = new CreateIngredientCommand(
            validRequest.getName(),
            validRequest.getDescription(),
            validRequest.getQuantity(),
            validRequest.getUnit(),
            validRequest.getSupplier(),
            BigDecimal.ZERO,
            validRequest.getPrice()
        );

        ingredientResponse = IngredientResponse.builder()
            .id(testId)
            .name(validRequest.getName())
            .description(validRequest.getDescription())
            .quantity(validRequest.getQuantity())
            .unit(validRequest.getUnit())
            .price(validRequest.getPrice())
            .supplier(validRequest.getSupplier())
            .minimumStock(BigDecimal.ZERO)
            .build();

        responseDto = new IngredientResponseDto(
            testId,
            validRequest.getName(),
            validRequest.getDescription(),
            validRequest.getQuantity(),
            validRequest.getUnit(),
            validRequest.getSupplier(),
            BigDecimal.ZERO
        );

        when(ingredientMapper.toCommand(any(CreateIngredientRequestDto.class))).thenReturn(command);
        when(ingredientMapper.toResponseDto(any(IngredientResponse.class))).thenReturn(responseDto);
    }

    @Test
    void createIngredient_Success() throws Exception {
        when(createIngredientUseCase.createIngredient(any())).thenReturn(ingredientResponse);

        mockMvc.perform(post("/api/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value(validRequest.getName()));

        verify(createIngredientUseCase).createIngredient(any());
    }

    @Test
    void createIngredient_InvalidRequest() throws Exception {
        // Arrange
        CreateIngredientRequestDto invalidRequest = new CreateIngredientRequestDto();
        // No establecemos ningún campo para que sea inválido

        // Act & Assert
        mockMvc.perform(post("/api/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(createIngredientUseCase, never()).createIngredient(any());
    }

    @Test
    void getAllIngredients_Success() throws Exception {
        when(getIngredientUseCase.getAllIngredients())
                .thenReturn(Arrays.asList(ingredientResponse));
        when(ingredientMapper.toResponseDto(ingredientResponse)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testId.toString()))
                .andExpect(jsonPath("$[0].name").value(validRequest.getName()));

        verify(getIngredientUseCase).getAllIngredients();
    }

    @Test
    void getIngredientById_Success() throws Exception {
        when(getIngredientUseCase.getIngredientById(testId)).thenReturn(ingredientResponse);

        mockMvc.perform(get("/api/v1/ingredients/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value(validRequest.getName()));

        verify(getIngredientUseCase).getIngredientById(testId);
    }

    @Test
    void getIngredientById_NotFound() throws Exception {
        when(getIngredientUseCase.getIngredientById(testId))
                .thenThrow(new BusinessException("Ingredient not found"));

        mockMvc.perform(get("/api/v1/ingredients/{id}", testId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ingredient not found"));
    }

    @Test
    void updateIngredient_Success() throws Exception {
        when(createIngredientUseCase.updateIngredient(eq(testId), any()))
                .thenReturn(ingredientResponse);

        mockMvc.perform(put("/api/v1/ingredients/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testId.toString()))
                .andExpect(jsonPath("$.name").value(validRequest.getName()));

        verify(createIngredientUseCase).updateIngredient(eq(testId), any());
    }

    @Test
    void updateIngredient_NotFound() throws Exception {
        when(createIngredientUseCase.updateIngredient(eq(testId), any()))
                .thenThrow(new BusinessException("Ingredient not found"));

        mockMvc.perform(put("/api/v1/ingredients/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ingredient not found"));
    }

    @Test
    void updateIngredient_InvalidRequest() throws Exception {
        CreateIngredientRequestDto invalidRequest = new CreateIngredientRequestDto();

        mockMvc.perform(put("/api/v1/ingredients/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(createIngredientUseCase, never()).updateIngredient(any(), any());
    }

    @Test
    void deleteIngredient_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/ingredients/{id}", testId))
                .andExpect(status().isNoContent());

        verify(createIngredientUseCase).deleteIngredient(testId);
    }

    @Test
    void deleteIngredient_NotFound() throws Exception {
        doThrow(new BusinessException("Ingredient not found"))
            .when(createIngredientUseCase).deleteIngredient(testId);

        mockMvc.perform(delete("/api/v1/ingredients/{id}", testId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ingredient not found"));
    }

    @Test
    void getIngredientsBySupplier_Success() throws Exception {
        when(getIngredientUseCase.getIngredientsBySupplier("Proveedor A"))
                .thenReturn(Arrays.asList(ingredientResponse));

        mockMvc.perform(get("/api/v1/ingredients/supplier/{supplier}", "Proveedor A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testId.toString()))
                .andExpect(jsonPath("$[0].name").value(validRequest.getName()));

        verify(getIngredientUseCase).getIngredientsBySupplier("Proveedor A");
    }

    @Test
    void getIngredientsBySupplier_EmptyList() throws Exception {
        when(getIngredientUseCase.getIngredientsBySupplier("Proveedor Inexistente"))
                .thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/ingredients/supplier/{supplier}", "Proveedor Inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(getIngredientUseCase).getIngredientsBySupplier("Proveedor Inexistente");
    }

    @Test
    void createIngredient_WithInvalidPrice() throws Exception {
        // Arrange
        CreateIngredientRequestDto requestWithInvalidPrice = new CreateIngredientRequestDto();
        requestWithInvalidPrice.setName("Harina");
        requestWithInvalidPrice.setDescription("Harina de trigo");
        requestWithInvalidPrice.setQuantity(new BigDecimal("1000"));
        requestWithInvalidPrice.setUnit("gramos");
        requestWithInvalidPrice.setPrice(new BigDecimal("-1.00")); // Precio negativo
        requestWithInvalidPrice.setSupplier("Proveedor A");

        // Act & Assert
        mockMvc.perform(post("/api/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithInvalidPrice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createIngredient_WithInvalidQuantity() throws Exception {
        // Arrange
        CreateIngredientRequestDto requestWithInvalidQuantity = new CreateIngredientRequestDto();
        requestWithInvalidQuantity.setName("Harina");
        requestWithInvalidQuantity.setDescription("Harina de trigo");
        requestWithInvalidQuantity.setQuantity(new BigDecimal("-1000")); // Cantidad negativa
        requestWithInvalidQuantity.setUnit("gramos");
        requestWithInvalidQuantity.setPrice(new BigDecimal("2.50"));
        requestWithInvalidQuantity.setSupplier("Proveedor A");

        // Act & Assert
        mockMvc.perform(post("/api/v1/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithInvalidQuantity)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void updateIngredient_WithInvalidPrice() throws Exception {
        // Arrange
        CreateIngredientRequestDto requestWithInvalidPrice = new CreateIngredientRequestDto();
        requestWithInvalidPrice.setName("Harina");
        requestWithInvalidPrice.setDescription("Harina de trigo");
        requestWithInvalidPrice.setQuantity(new BigDecimal("1000"));
        requestWithInvalidPrice.setUnit("gramos");
        requestWithInvalidPrice.setPrice(new BigDecimal("-1.00")); // Precio negativo
        requestWithInvalidPrice.setSupplier("Proveedor A");

        // Act & Assert
        mockMvc.perform(put("/api/v1/ingredients/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithInvalidPrice)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getIngredientsBySupplier_WithInvalidSupplier() throws Exception {
        // Arrange
        when(getIngredientUseCase.getIngredientsBySupplier("Proveedor Inválido"))
            .thenThrow(new BusinessException("Proveedor no válido"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/ingredients/supplier/{supplier}", "Proveedor Inválido"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Proveedor no válido"));
    }
} 