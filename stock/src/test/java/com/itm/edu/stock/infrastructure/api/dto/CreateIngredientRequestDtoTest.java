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

    private CreateIngredientRequestDto createValidDto() {
        CreateIngredientRequestDto dto = new CreateIngredientRequestDto();
        dto.setName("Harina");
        dto.setDescription("Harina de trigo");
        dto.setQuantity(new BigDecimal("1000"));
        dto.setUnit("gramos");
        dto.setPrice(new BigDecimal("2.50"));
        dto.setSupplier("Proveedor A");
        return dto;
    }

    @Test
    void whenCreateValidIngredientDto_thenNoViolations() {
        // Given
        CreateIngredientRequestDto dto = createValidDto();

        // When
        var violations = validator.validate(dto);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenCreateIngredientDtoWithoutName_thenViolation() {
        // Given
        CreateIngredientRequestDto dto = createValidDto();
        dto.setName(null);

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
        CreateIngredientRequestDto dto = createValidDto();
        dto.setQuantity(null);

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
        CreateIngredientRequestDto dto = createValidDto();
        dto.setQuantity(new BigDecimal("-1000"));

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La cantidad debe ser mayor o igual a 0", violations.iterator().next().getMessage());
    }

    @Test
    void whenCreateIngredientDtoWithoutUnit_thenViolation() {
        // Given
        CreateIngredientRequestDto dto = createValidDto();
        dto.setUnit(null);

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La unidad es requerida", violations.iterator().next().getMessage());
    }

    @Test
    void whenCreateIngredientDtoWithoutPrice_thenViolation() {
        // Given
        CreateIngredientRequestDto dto = createValidDto();
        dto.setPrice(null);

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("El precio es requerido", violations.iterator().next().getMessage());
    }

    @Test
    void whenCreateIngredientDtoWithNegativePrice_thenViolation() {
        // Given
        CreateIngredientRequestDto dto = createValidDto();
        dto.setPrice(new BigDecimal("-2.50"));

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("El precio debe ser mayor o igual a 0", violations.iterator().next().getMessage());
    }

    @Test
    void whenCreateIngredientDtoWithoutSupplier_thenViolation() {
        // Given
        CreateIngredientRequestDto dto = createValidDto();
        dto.setSupplier(null);

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("El proveedor es requerido", violations.iterator().next().getMessage());
    }
} 