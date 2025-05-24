package com.itm.edu.stock.application.services;

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

import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import com.itm.edu.stock.infrastructure.persistence.mapper.IngredientPersistenceMapper;
import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientPersistenceMapper ingredientMapper;

    @InjectMocks
    private IngredientService ingredientService;

    private UUID testId;
    private IngredientDto testDto;
    private CreateIngredientCommand testCommand;
    private IngredientResponse testResponse;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        
        testCommand = new CreateIngredientCommand(
            "Harina",
            "Harina de trigo",
            new BigDecimal("1000"),
            "gramos",
            "Proveedor A",
            new BigDecimal("100"),
            new BigDecimal("2.50")
        );

        testDto = IngredientDto.builder()
            .id(testId)
            .name(testCommand.getName())
            .description(testCommand.getDescription())
            .quantity(testCommand.getQuantity())
            .unit(testCommand.getUnit())
            .price(testCommand.getPrice())
            .supplier(testCommand.getSupplier())
            .minimumStock(testCommand.getMinimumStock())
            .build();

        testResponse = IngredientResponse.builder()
            .id(testId)
            .name(testCommand.getName())
            .description(testCommand.getDescription())
            .quantity(testCommand.getQuantity())
            .unit(testCommand.getUnit())
            .price(testCommand.getPrice())
            .supplier(testCommand.getSupplier())
            .minimumStock(testCommand.getMinimumStock())
            .build();
    }

    @Test
    void testCreateIngredient() {
        // Arrange
        when(ingredientMapper.fromCommand(testCommand)).thenReturn(testDto);
        when(ingredientRepository.save(testDto)).thenReturn(testResponse);

        // Act
        IngredientResponse result = ingredientService.createIngredient(testCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        assertEquals(testResponse.getName(), result.getName());
        assertEquals(testResponse.getDescription(), result.getDescription());
        assertEquals(testResponse.getQuantity(), result.getQuantity());
        assertEquals(testResponse.getUnit(), result.getUnit());
        assertEquals(testResponse.getPrice(), result.getPrice());
        assertEquals(testResponse.getSupplier(), result.getSupplier());
        assertEquals(testResponse.getMinimumStock(), result.getMinimumStock());
        
        verify(ingredientMapper).fromCommand(testCommand);
        verify(ingredientRepository).save(testDto);
    }

    @Test
    void testGetAllIngredients() {
        // Arrange
        IngredientResponse response2 = IngredientResponse.builder()
            .id(UUID.randomUUID())
            .name("Azúcar")
            .description("Azúcar blanca")
            .quantity(new BigDecimal("500"))
            .unit("gramos")
            .price(new BigDecimal("1.50"))
            .supplier("Proveedor B")
            .minimumStock(new BigDecimal("50"))
            .build();

        List<IngredientResponse> responses = Arrays.asList(testResponse, response2);
        when(ingredientRepository.findAll()).thenReturn(responses);

        // Act
        List<IngredientResponse> results = ingredientService.getAllIngredients();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(testResponse.getId(), results.get(0).getId());
        assertEquals(testResponse.getName(), results.get(0).getName());
        assertEquals(testResponse.getDescription(), results.get(0).getDescription());
        assertEquals(testResponse.getQuantity(), results.get(0).getQuantity());
        assertEquals(testResponse.getUnit(), results.get(0).getUnit());
        assertEquals(testResponse.getPrice(), results.get(0).getPrice());
        assertEquals(testResponse.getSupplier(), results.get(0).getSupplier());
        assertEquals(testResponse.getMinimumStock(), results.get(0).getMinimumStock());
        
        verify(ingredientRepository).findAll();
    }

    @Test
    void testGetIngredientById() {
        // Arrange
        when(ingredientRepository.findById(testId)).thenReturn(Optional.of(testResponse));

        // Act
        IngredientResponse result = ingredientService.getIngredientById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        assertEquals(testResponse.getName(), result.getName());
        assertEquals(testResponse.getDescription(), result.getDescription());
        assertEquals(testResponse.getQuantity(), result.getQuantity());
        assertEquals(testResponse.getUnit(), result.getUnit());
        assertEquals(testResponse.getPrice(), result.getPrice());
        assertEquals(testResponse.getSupplier(), result.getSupplier());
        assertEquals(testResponse.getMinimumStock(), result.getMinimumStock());
        
        verify(ingredientRepository).findById(testId);
    }
} 