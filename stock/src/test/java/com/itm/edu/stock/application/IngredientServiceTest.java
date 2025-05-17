package com.itm.edu.stock.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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

import com.itm.edu.stock.application.services.IngredientService;
import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.domain.repository.IngredientRepository;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import com.itm.edu.stock.infrastructure.api.mapper.IngredientMapper;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientMapper ingredientMapper;

    @InjectMocks
    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateIngredient() {
        CreateIngredientRequestDto request = new CreateIngredientRequestDto();
        request.setName("Harina");
        request.setDescription("Harina de trigo");
        request.setQuantity(new BigDecimal("1000"));
        request.setUnit("gramos");
        request.setPrice(new BigDecimal("2.50"));
        request.setSupplier("Proveedor A");

        Ingredient savedIngredient = new Ingredient();
        savedIngredient.setId(UUID.randomUUID());
        savedIngredient.setName(request.getName());
        savedIngredient.setDescription(request.getDescription());
        savedIngredient.setQuantity(request.getQuantity());
        savedIngredient.setUnit(request.getUnit());
        savedIngredient.setPrice(request.getPrice());
        savedIngredient.setSupplier(request.getSupplier());

        IngredientResponseDto responseDto = new IngredientResponseDto();
        responseDto.setId(savedIngredient.getId());
        responseDto.setName(savedIngredient.getName());
        responseDto.setDescription(savedIngredient.getDescription());
        responseDto.setQuantity(savedIngredient.getQuantity());
        responseDto.setUnit(savedIngredient.getUnit());
        responseDto.setPrice(savedIngredient.getPrice());
        responseDto.setSupplier(savedIngredient.getSupplier());

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(savedIngredient);
        when(ingredientMapper.toEntity(any(CreateIngredientRequestDto.class))).thenReturn(savedIngredient);
        when(ingredientMapper.toDto(any(Ingredient.class))).thenReturn(responseDto);

        IngredientResponseDto result = ingredientService.createIngredient(request);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getQuantity(), result.getQuantity());
        assertEquals(request.getUnit(), result.getUnit());
        assertEquals(request.getPrice(), result.getPrice());
        assertEquals(request.getSupplier(), result.getSupplier());
    }

    @Test
    void testGetAllIngredients() {
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(UUID.randomUUID());
        ingredient1.setName("Harina");
        ingredient1.setQuantity(new BigDecimal("1000"));
        ingredient1.setUnit("gramos");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(UUID.randomUUID());
        ingredient2.setName("Azúcar");
        ingredient2.setQuantity(new BigDecimal("500"));
        ingredient2.setUnit("gramos");

        IngredientResponseDto dto1 = new IngredientResponseDto();
        dto1.setId(ingredient1.getId());
        dto1.setName(ingredient1.getName());
        dto1.setQuantity(ingredient1.getQuantity());
        dto1.setUnit(ingredient1.getUnit());

        IngredientResponseDto dto2 = new IngredientResponseDto();
        dto2.setId(ingredient2.getId());
        dto2.setName(ingredient2.getName());
        dto2.setQuantity(ingredient2.getQuantity());
        dto2.setUnit(ingredient2.getUnit());

        when(ingredientRepository.findAll()).thenReturn(Arrays.asList(ingredient1, ingredient2));
        when(ingredientMapper.toDto(ingredient1)).thenReturn(dto1);
        when(ingredientMapper.toDto(ingredient2)).thenReturn(dto2);

        List<IngredientResponseDto> results = ingredientService.getAllIngredients();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(ingredient1.getName(), results.get(0).getName());
        assertEquals(ingredient2.getName(), results.get(1).getName());
    }

    @Test
    void testGetIngredientById() {
        UUID id = UUID.randomUUID();
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName("Harina");
        ingredient.setQuantity(new BigDecimal("1000"));
        ingredient.setUnit("gramos");

        IngredientResponseDto dto = new IngredientResponseDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setQuantity(ingredient.getQuantity());
        dto.setUnit(ingredient.getUnit());

        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        when(ingredientMapper.toDto(ingredient)).thenReturn(dto);

        IngredientResponseDto result = ingredientService.getIngredientById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(ingredient.getName(), result.getName());
        assertEquals(ingredient.getQuantity(), result.getQuantity());
        assertEquals(ingredient.getUnit(), result.getUnit());
    }
}