package com.wilson.stock.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wilson.stock.application.services.IngredientService;
import com.wilson.stock.domain.entities.Ingredient;
import com.wilson.stock.domain.repository.IngredientRepository;
import com.wilson.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.wilson.stock.domain.valueobjects.Quantity;
import com.wilson.stock.domain.valueobjects.Unit;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        ingredientService = new IngredientService(ingredientRepository);
    }

    @Test
    void createIngredient_ShouldCreateAndSaveIngredient() {
        // Arrange
        CreateIngredientRequestDto request = new CreateIngredientRequestDto();
        request.setName("Harina");
        request.setDescription("Harina de trigo");
        request.setQuantity(new BigDecimal("1000"));
        request.setUnit("gramos");

        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setId(UUID.randomUUID());
        savedIngredient.setName(request.getName());
        savedIngredient.setDescription(request.getDescription());
        savedIngredient.setQuantity(new Quantity(request.getQuantity()));
        savedIngredient.setUnit(new Unit(request.getUnit()));

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(savedIngredient);

        // Act
        Ingredient result = ingredientService.createIngredient(request);

        // Assert
        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getQuantity(), result.getQuantity().getValue());
        assertEquals(request.getUnit(), result.getUnit().getValue());
        verify(ingredientRepository).save(any(Ingredient.class));
    }

    @Test
    void getAllIngredients_ShouldReturnAllIngredients() {
        // Arrange
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(UUID.randomUUID());
        ingredient1.setName("Harina");
        ingredient1.setQuantity(new Quantity(new BigDecimal("1000")));
        ingredient1.setUnit(new Unit("gramos"));

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(UUID.randomUUID());
        ingredient2.setName("Azúcar");
        ingredient2.setQuantity(new Quantity(new BigDecimal("500")));
        ingredient2.setUnit(new Unit("gramos"));

        when(ingredientRepository.findAll()).thenReturn(List.of(ingredient1, ingredient2));

        // Act
        List<Ingredient> result = ingredientService.getAllIngredients();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ingredientRepository).findAll();
    }

    @Test
    void getIngredientById_WhenIngredientExists_ShouldReturnIngredient() {
        // Arrange
        UUID id = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName("Harina");
        ingredient.setQuantity(new Quantity(new BigDecimal("1000")));
        ingredient.setUnit(new Unit("gramos"));

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));

        // Act
        Ingredient result = ingredientService.getIngredientById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Harina", result.getName());
        verify(ingredientRepository).findById(id);
    }

    @Test
    void getIngredientById_WhenIngredientDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            ingredientService.getIngredientById(id);
        });
        verify(ingredientRepository).findById(id);
    }
}