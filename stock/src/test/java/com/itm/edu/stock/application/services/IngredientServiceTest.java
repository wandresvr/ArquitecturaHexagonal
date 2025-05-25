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
import com.itm.edu.stock.domain.exception.BusinessException;

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

    @Test
    void testCreateIngredient_NullCommand() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> ingredientService.createIngredient(null));
        verify(ingredientMapper, never()).fromCommand(any());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void testCreateIngredient_InvalidData() {
        // Arrange
        CreateIngredientCommand invalidCommand = new CreateIngredientCommand(
            "",  // nombre vacío
            null, // descripción nula
            BigDecimal.ZERO, // cantidad cero
            "",  // unidad vacía
            null, // proveedor nulo
            BigDecimal.valueOf(-1), // stock mínimo negativo
            BigDecimal.valueOf(-1)  // precio negativo
        );

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ingredientService.createIngredient(invalidCommand));
        verify(ingredientMapper, never()).fromCommand(any());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void testGetAllIngredients_EmptyList() {
        // Arrange
        when(ingredientRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<IngredientResponse> results = ingredientService.getAllIngredients();

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(ingredientRepository).findAll();
    }

    @Test
    void testGetIngredientById_NotFound() {
        // Arrange
        when(ingredientRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ingredientService.getIngredientById(testId));
        verify(ingredientRepository).findById(testId);
    }

    @Test
    void testGetIngredientById_NullId() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> ingredientService.getIngredientById(null));
        verify(ingredientRepository, never()).findById(any());
    }

    @Test
    void testCreateIngredient_WithMinimumValues() {
        // Arrange
        CreateIngredientCommand minCommand = new CreateIngredientCommand(
            "Test",
            "Test Description",
            BigDecimal.ONE,
            "unit",
            "supplier",
            BigDecimal.ZERO,
            BigDecimal.ONE
        );

        IngredientDto minDto = IngredientDto.builder()
            .id(UUID.randomUUID())
            .name(minCommand.getName())
            .description(minCommand.getDescription())
            .quantity(minCommand.getQuantity())
            .unit(minCommand.getUnit())
            .price(minCommand.getPrice())
            .supplier(minCommand.getSupplier())
            .minimumStock(minCommand.getMinimumStock())
            .build();

        IngredientResponse minResponse = IngredientResponse.builder()
            .id(minDto.getId())
            .name(minCommand.getName())
            .description(minCommand.getDescription())
            .quantity(minCommand.getQuantity())
            .unit(minCommand.getUnit())
            .price(minCommand.getPrice())
            .supplier(minCommand.getSupplier())
            .minimumStock(minCommand.getMinimumStock())
            .build();

        when(ingredientMapper.fromCommand(minCommand)).thenReturn(minDto);
        when(ingredientRepository.save(minDto)).thenReturn(minResponse);

        // Act
        IngredientResponse result = ingredientService.createIngredient(minCommand);

        // Assert
        assertNotNull(result);
        assertEquals(minResponse.getId(), result.getId());
        assertEquals(minResponse.getName(), result.getName());
        assertEquals(minResponse.getDescription(), result.getDescription());
        assertEquals(minResponse.getQuantity(), result.getQuantity());
        assertEquals(minResponse.getUnit(), result.getUnit());
        assertEquals(minResponse.getPrice(), result.getPrice());
        assertEquals(minResponse.getSupplier(), result.getSupplier());
        assertEquals(minResponse.getMinimumStock(), result.getMinimumStock());
        
        verify(ingredientMapper).fromCommand(minCommand);
        verify(ingredientRepository).save(minDto);
    }

    @Test
    void testCreateIngredient_WithMaximumValues() {
        // Arrange
        CreateIngredientCommand maxCommand = new CreateIngredientCommand(
            "Test Ingredient With Very Long Name That Should Still Be Valid",
            "Test Description With Maximum Length That Should Be Accepted By The System And Still Process Correctly",
            BigDecimal.valueOf(999999.99),
            "kilogramos",
            "Test Supplier With Very Long Name That Should Still Be Valid",
            BigDecimal.valueOf(999999.99),
            BigDecimal.valueOf(999999.99)
        );

        IngredientDto maxDto = IngredientDto.builder()
            .id(UUID.randomUUID())
            .name(maxCommand.getName())
            .description(maxCommand.getDescription())
            .quantity(maxCommand.getQuantity())
            .unit(maxCommand.getUnit())
            .price(maxCommand.getPrice())
            .supplier(maxCommand.getSupplier())
            .minimumStock(maxCommand.getMinimumStock())
            .build();

        IngredientResponse maxResponse = IngredientResponse.builder()
            .id(maxDto.getId())
            .name(maxCommand.getName())
            .description(maxCommand.getDescription())
            .quantity(maxCommand.getQuantity())
            .unit(maxCommand.getUnit())
            .price(maxCommand.getPrice())
            .supplier(maxCommand.getSupplier())
            .minimumStock(maxCommand.getMinimumStock())
            .build();

        when(ingredientMapper.fromCommand(maxCommand)).thenReturn(maxDto);
        when(ingredientRepository.save(maxDto)).thenReturn(maxResponse);

        // Act
        IngredientResponse result = ingredientService.createIngredient(maxCommand);

        // Assert
        assertNotNull(result);
        assertEquals(maxResponse.getId(), result.getId());
        assertEquals(maxResponse.getName(), result.getName());
        assertEquals(maxResponse.getDescription(), result.getDescription());
        assertEquals(maxResponse.getQuantity(), result.getQuantity());
        assertEquals(maxResponse.getUnit(), result.getUnit());
        assertEquals(maxResponse.getPrice(), result.getPrice());
        assertEquals(maxResponse.getSupplier(), result.getSupplier());
        assertEquals(maxResponse.getMinimumStock(), result.getMinimumStock());
        
        verify(ingredientMapper).fromCommand(maxCommand);
        verify(ingredientRepository).save(maxDto);
    }

    @Test
    void testUpdateIngredient_Success() {
        // Arrange
        when(ingredientRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.save(any(IngredientDto.class))).thenReturn(testResponse);

        // Act
        IngredientResponse result = ingredientService.updateIngredient(testId, testCommand);

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
        verify(ingredientRepository).save(any(IngredientDto.class));
    }

    @Test
    void testUpdateIngredient_NotFound() {
        // Arrange
        when(ingredientRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> ingredientService.updateIngredient(testId, testCommand));
        verify(ingredientRepository).findById(testId);
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void testUpdateIngredient_PartialUpdate() {
        // Arrange
        CreateIngredientCommand partialCommand = new CreateIngredientCommand(
            "Nuevo Nombre",
            null,
            null,
            null,
            null,
            null,
            null
        );

        when(ingredientRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.save(any(IngredientDto.class))).thenReturn(
            IngredientResponse.builder()
                .id(testId)
                .name("Nuevo Nombre")
                .description(testResponse.getDescription())
                .quantity(testResponse.getQuantity())
                .unit(testResponse.getUnit())
                .price(testResponse.getPrice())
                .supplier(testResponse.getSupplier())
                .minimumStock(testResponse.getMinimumStock())
                .build()
        );

        // Act
        IngredientResponse result = ingredientService.updateIngredient(testId, partialCommand);

        // Assert
        assertNotNull(result);
        assertEquals("Nuevo Nombre", result.getName());
        assertEquals(testResponse.getDescription(), result.getDescription());
        assertEquals(testResponse.getQuantity(), result.getQuantity());
        assertEquals(testResponse.getUnit(), result.getUnit());
        assertEquals(testResponse.getPrice(), result.getPrice());
        assertEquals(testResponse.getSupplier(), result.getSupplier());
        assertEquals(testResponse.getMinimumStock(), result.getMinimumStock());
    }

    @Test
    void testDeleteIngredient_Success() {
        // Act
        ingredientService.deleteIngredient(testId);

        // Assert
        verify(ingredientRepository).deleteById(testId);
    }

    @Test
    void testGetIngredientsBySupplier_Success() {
        // Arrange
        String supplier = "Proveedor A";
        List<IngredientResponse> expectedResponses = Arrays.asList(testResponse);
        when(ingredientRepository.findBySupplier(supplier)).thenReturn(expectedResponses);

        // Act
        List<IngredientResponse> results = ingredientService.getIngredientsBySupplier(supplier);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testResponse.getId(), results.get(0).getId());
        assertEquals(testResponse.getName(), results.get(0).getName());
        verify(ingredientRepository).findBySupplier(supplier);
    }

    @Test
    void testGetIngredientsBySupplier_EmptyList() {
        // Arrange
        String supplier = "Proveedor Inexistente";
        when(ingredientRepository.findBySupplier(supplier)).thenReturn(Arrays.asList());

        // Act
        List<IngredientResponse> results = ingredientService.getIngredientsBySupplier(supplier);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(ingredientRepository).findBySupplier(supplier);
    }

    @Test
    void testUpdateIngredientQuantity_Success() {
        // Arrange
        BigDecimal newQuantity = new BigDecimal("500");
        when(ingredientRepository.findById(testId)).thenReturn(Optional.of(testResponse));
        when(ingredientRepository.save(any(IngredientDto.class))).thenReturn(
            IngredientResponse.builder()
                .id(testId)
                .name(testResponse.getName())
                .description(testResponse.getDescription())
                .quantity(newQuantity)
                .unit(testResponse.getUnit())
                .price(testResponse.getPrice())
                .supplier(testResponse.getSupplier())
                .minimumStock(testResponse.getMinimumStock())
                .build()
        );

        // Act
        ingredientService.updateIngredientQuantity(testId, newQuantity);

        // Assert
        verify(ingredientRepository).findById(testId);
        verify(ingredientRepository).save(any(IngredientDto.class));
    }

    @Test
    void testUpdateIngredientQuantity_NotFound() {
        // Arrange
        BigDecimal newQuantity = new BigDecimal("500");
        when(ingredientRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> ingredientService.updateIngredientQuantity(testId, newQuantity));
        verify(ingredientRepository).findById(testId);
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void testCreateIngredient_InvalidQuantity() {
        // Arrange
        CreateIngredientCommand invalidCommand = new CreateIngredientCommand(
            "Test",
            "Description",
            BigDecimal.ZERO,  // cantidad inválida
            "unit",
            "supplier",
            BigDecimal.ZERO,
            BigDecimal.ONE
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> ingredientService.createIngredient(invalidCommand));
        assertEquals("La cantidad debe ser mayor que cero", exception.getMessage());
        verify(ingredientMapper, never()).fromCommand(any());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void testCreateIngredient_InvalidPrice() {
        // Arrange
        CreateIngredientCommand invalidCommand = new CreateIngredientCommand(
            "Test",
            "Description",
            BigDecimal.ONE,
            "unit",
            "supplier",
            BigDecimal.ZERO,
            new BigDecimal("-1")  // precio inválido
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> ingredientService.createIngredient(invalidCommand));
        assertEquals("El precio no puede ser negativo", exception.getMessage());
        verify(ingredientMapper, never()).fromCommand(any());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void testCreateIngredient_EmptyName() {
        // Arrange
        CreateIngredientCommand invalidCommand = new CreateIngredientCommand(
            "",  // nombre vacío
            "Description",
            BigDecimal.ONE,
            "unit",
            "supplier",
            BigDecimal.ZERO,
            BigDecimal.ONE
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> ingredientService.createIngredient(invalidCommand));
        assertEquals("El nombre del ingrediente es requerido", exception.getMessage());
        verify(ingredientMapper, never()).fromCommand(any());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void testCreateIngredient_EmptyUnit() {
        // Arrange
        CreateIngredientCommand invalidCommand = new CreateIngredientCommand(
            "Test",
            "Description",
            BigDecimal.ONE,
            "",  // unidad vacía
            "supplier",
            BigDecimal.ZERO,
            BigDecimal.ONE
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> ingredientService.createIngredient(invalidCommand));
        assertEquals("La unidad de medida es requerida", exception.getMessage());
        verify(ingredientMapper, never()).fromCommand(any());
        verify(ingredientRepository, never()).save(any());
    }

    @Test
    void testCreateIngredient_EmptySupplier() {
        // Arrange
        CreateIngredientCommand invalidCommand = new CreateIngredientCommand(
            "Test",
            "Description",
            BigDecimal.ONE,
            "unit",
            "",  // proveedor vacío
            BigDecimal.ZERO,
            BigDecimal.ONE
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> ingredientService.createIngredient(invalidCommand));
        assertEquals("El proveedor es requerido", exception.getMessage());
        verify(ingredientMapper, never()).fromCommand(any());
        verify(ingredientRepository, never()).save(any());
    }
} 