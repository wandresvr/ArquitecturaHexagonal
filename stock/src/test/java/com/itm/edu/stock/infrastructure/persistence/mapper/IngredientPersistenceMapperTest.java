package com.itm.edu.stock.infrastructure.persistence.mapper;

import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import com.itm.edu.stock.infrastructure.persistence.entity.IngredientJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IngredientPersistenceMapperTest {

    private IngredientPersistenceMapper mapper;
    private UUID testId;
    private IngredientJpaEntity testEntity;
    private IngredientDto testDto;

    @BeforeEach
    void setUp() {
        mapper = new IngredientPersistenceMapper();
        testId = UUID.randomUUID();

        testEntity = IngredientJpaEntity.builder()
            .id(testId)
            .name("Harina")
            .description("Harina de trigo")
            .quantity(new BigDecimal("1000"))
            .unit("gramos")
            .price(new BigDecimal("2.50"))
            .supplier("Proveedor A")
            .minimumStock(BigDecimal.ZERO)
            .build();

        testDto = IngredientDto.builder()
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
    void toDto_Success() {
        // Act
        IngredientDto result = mapper.toDto(testEntity);

        // Assert
        assertNotNull(result);
        assertEquals(testEntity.getId(), result.getId());
        assertEquals(testEntity.getName(), result.getName());
        assertEquals(testEntity.getDescription(), result.getDescription());
        assertEquals(testEntity.getQuantity(), result.getQuantity());
        assertEquals(testEntity.getUnit(), result.getUnit());
        assertEquals(testEntity.getPrice(), result.getPrice());
        assertEquals(testEntity.getSupplier(), result.getSupplier());
        assertEquals(testEntity.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void toDto_NullEntity() {
        // Arrange & Act
        IngredientDto result = mapper.toDto(null);

        // Assert
        assertNull(result, "El resultado debería ser null cuando la entidad es null");
    }

    @Test
    void toEntity_Success() {
        // Act
        IngredientJpaEntity result = mapper.toEntity(testDto);

        // Assert
        assertNotNull(result);
        assertEquals(testDto.getId(), result.getId());
        assertEquals(testDto.getName(), result.getName());
        assertEquals(testDto.getDescription(), result.getDescription());
        assertEquals(testDto.getQuantity(), result.getQuantity());
        assertEquals(testDto.getUnit(), result.getUnit());
        assertEquals(testDto.getPrice(), result.getPrice());
        assertEquals(testDto.getSupplier(), result.getSupplier());
        assertEquals(testDto.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void toEntity_NullDto() {
        // Arrange & Act
        IngredientJpaEntity result = mapper.toEntity(null);

        // Assert
        assertNull(result, "El resultado debería ser null cuando el DTO es null");
    }

    @Test
    void toResponse_Success() {
        // Act
        IngredientResponse result = mapper.toResponse(testDto);

        // Assert
        assertNotNull(result);
        assertEquals(testDto.getId(), result.getId());
        assertEquals(testDto.getName(), result.getName());
        assertEquals(testDto.getDescription(), result.getDescription());
        assertEquals(testDto.getQuantity(), result.getQuantity());
        assertEquals(testDto.getUnit(), result.getUnit());
        assertEquals(testDto.getPrice(), result.getPrice());
        assertEquals(testDto.getSupplier(), result.getSupplier());
        assertEquals(testDto.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void toResponse_NullDto() {
        // Arrange & Act
        IngredientResponse result = mapper.toResponse(null);

        // Assert
        assertNull(result, "El resultado debería ser null cuando el DTO es null");
    }

    @Test
    void fromCommand_Success() {
        // Arrange
        CreateIngredientCommand command = new CreateIngredientCommand(
            "Harina",
            "Harina de trigo",
            new BigDecimal("1000"),
            "gramos",
            "Proveedor A",
            BigDecimal.ZERO,
            new BigDecimal("2.50")
        );

        // Act
        IngredientDto result = mapper.fromCommand(command);

        // Assert
        assertNotNull(result);
        assertEquals(command.getName(), result.getName());
        assertEquals(command.getDescription(), result.getDescription());
        assertEquals(command.getQuantity(), result.getQuantity());
        assertEquals(command.getUnit(), result.getUnit());
        assertEquals(command.getPrice(), result.getPrice());
        assertEquals(command.getSupplier(), result.getSupplier());
        assertEquals(command.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void fromCommand_NullCommand() {
        // Arrange & Act
        IngredientDto result = mapper.fromCommand(null);

        // Assert
        assertNull(result, "El resultado debería ser null cuando el comando es null");
    }

    @Test
    void fromResponse_Success() {
        // Arrange
        IngredientResponse response = IngredientResponse.builder()
            .id(testId)
            .name("Harina")
            .description("Harina de trigo")
            .quantity(new BigDecimal("1000"))
            .unit("gramos")
            .price(new BigDecimal("2.50"))
            .supplier("Proveedor A")
            .minimumStock(BigDecimal.ZERO)
            .build();

        // Act
        IngredientJpaEntity result = mapper.fromResponse(response);

        // Assert
        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getDescription(), result.getDescription());
        assertEquals(response.getQuantity(), result.getQuantity());
        assertEquals(response.getUnit(), result.getUnit());
        assertEquals(response.getPrice(), result.getPrice());
        assertEquals(response.getSupplier(), result.getSupplier());
        assertEquals(response.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void fromResponse_NullResponse() {
        // Arrange & Act
        IngredientJpaEntity result = mapper.fromResponse(null);

        // Assert
        assertNull(result, "El resultado debería ser null cuando la respuesta es null");
    }
} 