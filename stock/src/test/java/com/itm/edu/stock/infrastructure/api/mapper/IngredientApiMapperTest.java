package com.itm.edu.stock.infrastructure.api.mapper;

import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IngredientApiMapperTest {

    private IngredientApiMapper mapper;
    private UUID testId;
    private IngredientResponse testResponse;
    private CreateIngredientRequestDto testRequestDto;

    @BeforeEach
    void setUp() {
        mapper = new IngredientApiMapper();
        testId = UUID.randomUUID();

        testResponse = IngredientResponse.builder()
            .id(testId)
            .name("Harina")
            .description("Harina de trigo")
            .quantity(new BigDecimal("1000"))
            .unit("gramos")
            .price(new BigDecimal("2.50"))
            .supplier("Proveedor A")
            .minimumStock(BigDecimal.ZERO)
            .build();

        testRequestDto = new CreateIngredientRequestDto();
        testRequestDto.setName("Harina");
        testRequestDto.setDescription("Harina de trigo");
        testRequestDto.setQuantity(new BigDecimal("1000"));
        testRequestDto.setUnit("gramos");
        testRequestDto.setSupplier("Proveedor A");
        testRequestDto.setPrice(new BigDecimal("2.50"));
    }

    @Test
    void toResponseDto_Success() {
        // Act
        IngredientResponseDto result = mapper.toResponseDto(testResponse);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        assertEquals(testResponse.getName(), result.getName());
        assertEquals(testResponse.getDescription(), result.getDescription());
        assertEquals(testResponse.getQuantity(), result.getQuantity());
        assertEquals(testResponse.getUnit(), result.getUnit());
        assertEquals(testResponse.getSupplier(), result.getSupplier());
        assertEquals(testResponse.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void toResponseDto_NullResponse() {
        // Act
        IngredientResponseDto result = mapper.toResponseDto(null);

        // Assert
        assertNull(result, "El resultado debería ser null cuando la respuesta es null");
    }

    @Test
    void toCommand_Success() {
        // Act
        CreateIngredientCommand result = mapper.toCommand(testRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(testRequestDto.getName(), result.getName());
        assertEquals(testRequestDto.getDescription(), result.getDescription());
        assertEquals(testRequestDto.getQuantity(), result.getQuantity());
        assertEquals(testRequestDto.getUnit(), result.getUnit());
        assertEquals(testRequestDto.getSupplier(), result.getSupplier());
        assertEquals(BigDecimal.ZERO, result.getMinimumStock());
        assertEquals(testRequestDto.getPrice(), result.getPrice());
    }

    @Test
    void toCommand_NullRequest() {
        // Act
        CreateIngredientCommand result = mapper.toCommand(null);

        // Assert
        assertNull(result, "El resultado debería ser null cuando el request es null");
    }

    @Test
    void toCommand_WithEmptyValues() {
        // Arrange
        CreateIngredientRequestDto emptyRequestDto = new CreateIngredientRequestDto();
        emptyRequestDto.setName("");
        emptyRequestDto.setDescription("");
        emptyRequestDto.setQuantity(null);
        emptyRequestDto.setUnit("");
        emptyRequestDto.setSupplier("");
        emptyRequestDto.setPrice(null);

        // Act
        CreateIngredientCommand result = mapper.toCommand(emptyRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getName());
        assertEquals("", result.getDescription());
        assertNull(result.getQuantity());
        assertEquals("", result.getUnit());
        assertEquals("", result.getSupplier());
        assertEquals(BigDecimal.ZERO, result.getMinimumStock());
        assertNull(result.getPrice());
    }

    @Test
    void toCommand_WithMaximumValues() {
        // Arrange
        CreateIngredientRequestDto maxRequestDto = new CreateIngredientRequestDto();
        maxRequestDto.setName("Test Ingredient With Very Long Name That Should Still Be Valid");
        maxRequestDto.setDescription("Test Description With Maximum Length That Should Be Accepted By The System And Still Process Correctly");
        maxRequestDto.setQuantity(BigDecimal.valueOf(999999.99));
        maxRequestDto.setUnit("kilogramos");
        maxRequestDto.setSupplier("Test Supplier With Very Long Name That Should Still Be Valid");
        maxRequestDto.setPrice(BigDecimal.valueOf(999999.99));

        // Act
        CreateIngredientCommand result = mapper.toCommand(maxRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(maxRequestDto.getName(), result.getName());
        assertEquals(maxRequestDto.getDescription(), result.getDescription());
        assertEquals(maxRequestDto.getQuantity(), result.getQuantity());
        assertEquals(maxRequestDto.getUnit(), result.getUnit());
        assertEquals(maxRequestDto.getSupplier(), result.getSupplier());
        assertEquals(BigDecimal.ZERO, result.getMinimumStock());
        assertEquals(maxRequestDto.getPrice(), result.getPrice());
    }
} 