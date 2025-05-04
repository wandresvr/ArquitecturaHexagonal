package com.itm.edu.stock.infrastructure.api.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

class CreateIngredientRequestDtoTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void whenCreateValidIngredientDto_thenNoViolations() {
        // Given
        CreateIngredientRequestDto dto = new CreateIngredientRequestDto();
        dto.setName("Flour");
        dto.setDescription("All purpose flour");
        dto.setQuantity(new BigDecimal("1000"));
        dto.setUnit("g");

        // When
        var violations = validator.validate(dto);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenCreateIngredientDtoWithoutName_thenViolation() {
        // Given
        CreateIngredientRequestDto dto = new CreateIngredientRequestDto();
        dto.setDescription("All purpose flour");
        dto.setQuantity(new BigDecimal("1000"));
        dto.setUnit("g");

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("El nombre es requerido", violations.iterator().next().getMessage());
    }

    @Test
    void whenCreateIngredientDtoWithoutQuantity_thenViolation() {
        // Given
        CreateIngredientRequestDto dto = new CreateIngredientRequestDto();
        dto.setName("Flour");
        dto.setDescription("All purpose flour");
        dto.setUnit("g");

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La cantidad es requerida", violations.iterator().next().getMessage());
    }

    @Test
    void whenCreateIngredientDtoWithNegativeQuantity_thenViolation() {
        // Given
        CreateIngredientRequestDto dto = new CreateIngredientRequestDto();
        dto.setName("Flour");
        dto.setDescription("All purpose flour");
        dto.setQuantity(new BigDecimal("-1000"));
        dto.setUnit("g");

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La cantidad debe ser positiva", violations.iterator().next().getMessage());
    }

    @Test
    void whenCreateIngredientDtoWithoutUnit_thenViolation() {
        // Given
        CreateIngredientRequestDto dto = new CreateIngredientRequestDto();
        dto.setName("Flour");
        dto.setDescription("All purpose flour");
        dto.setQuantity(new BigDecimal("1000"));

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La unidad es requerida", violations.iterator().next().getMessage());
    }
} 