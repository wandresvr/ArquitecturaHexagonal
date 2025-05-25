package com.itm.edu.stock.infrastructure.mapper;

import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.domain.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IngredientDomainMapperTest {

    private IngredientDomainMapper mapper;
    private UUID testId;
    private CreateIngredientCommand testCommand;
    private Ingredient testIngredient;

    @BeforeEach
    void setUp() {
        mapper = new IngredientDomainMapper();
        testId = UUID.randomUUID();

        testCommand = new CreateIngredientCommand(
            "Harina",
            "Harina de trigo",
            new BigDecimal("1000"),
            "gramos",
            "Proveedor A",
            BigDecimal.ZERO,
            new BigDecimal("2.50")
        );

        testIngredient = Ingredient.builder()
            .id(testId)
            .name("Harina")
            .description("Harina de trigo")
            .quantity(new BigDecimal("1000"))
            .unit("gramos")
            .price(new BigDecimal("2.50"))
            .supplier("Proveedor A")
            .minimumStock(BigDecimal.ZERO)
            .build();
    }

    @Test
    void toEntity_Success() {
        // Act
        Ingredient result = mapper.toEntity(testCommand);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testCommand.getName(), result.getName());
        assertEquals(testCommand.getDescription(), result.getDescription());
        assertEquals(testCommand.getQuantity(), result.getQuantity());
        assertEquals(testCommand.getUnit(), result.getUnit());
        assertEquals(testCommand.getPrice(), result.getPrice());
        assertEquals(testCommand.getSupplier(), result.getSupplier());
        assertEquals(testCommand.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void toEntity_NullCommand() {
        // Act
        Ingredient result = mapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toResponse_Success() {
        // Act
        IngredientResponse result = mapper.toResponse(testIngredient);

        // Assert
        assertNotNull(result);
        assertEquals(testIngredient.getId(), result.getId());
        assertEquals(testIngredient.getName(), result.getName());
        assertEquals(testIngredient.getDescription(), result.getDescription());
        assertEquals(testIngredient.getQuantity(), result.getQuantity());
        assertEquals(testIngredient.getUnit(), result.getUnit());
        assertEquals(testIngredient.getPrice(), result.getPrice());
        assertEquals(testIngredient.getSupplier(), result.getSupplier());
        assertEquals(testIngredient.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void toResponse_NullIngredient() {
        // Act
        IngredientResponse result = mapper.toResponse(null);

        // Assert
        assertNull(result);
    }

    @Test
    void updateEntityFromCommand_Success() {
        // Act
        Ingredient result = mapper.updateEntityFromCommand(testIngredient, testCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testIngredient.getId(), result.getId());
        assertEquals(testCommand.getName(), result.getName());
        assertEquals(testCommand.getDescription(), result.getDescription());
        assertEquals(testCommand.getQuantity(), result.getQuantity());
        assertEquals(testCommand.getUnit(), result.getUnit());
        assertEquals(testCommand.getPrice(), result.getPrice());
        assertEquals(testCommand.getSupplier(), result.getSupplier());
        assertEquals(testCommand.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void updateEntityFromCommand_NullIngredient() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> mapper.updateEntityFromCommand(null, testCommand));
        assertEquals("El ingrediente no puede ser nulo", exception.getMessage());
    }

    @Test
    void updateEntityFromCommand_NullCommand() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
            () -> mapper.updateEntityFromCommand(testIngredient, null));
        assertEquals("El comando de actualización no puede ser nulo", exception.getMessage());
    }

    @Test
    void updateEntityFromCommand_WithEmptyValues() {
        // Arrange
        CreateIngredientCommand emptyCommand = new CreateIngredientCommand(
            "",
            "",
            null,
            "",
            "",
            null,
            null
        );

        // Act
        Ingredient result = mapper.updateEntityFromCommand(testIngredient, emptyCommand);

        // Assert
        assertNotNull(result);
        assertEquals(testIngredient.getId(), result.getId());
        assertEquals("", result.getName());
        assertEquals("", result.getDescription());
        assertNull(result.getQuantity());
        assertEquals("", result.getUnit());
        assertEquals("", result.getSupplier());
        assertNull(result.getMinimumStock());
        assertNull(result.getPrice());
    }
} 