package com.itm.edu.stock.infrastructure.persistence.adapter;

import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import com.itm.edu.stock.infrastructure.persistence.entity.IngredientJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.repository.IngredientJpaRepository;
import com.itm.edu.stock.infrastructure.persistence.mapper.IngredientPersistenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientRepositoryAdapterTest {

    @Mock
    private IngredientJpaRepository repository;

    @Mock
    private IngredientPersistenceMapper mapper;

    @InjectMocks
    private IngredientRepositoryAdapter adapter;

    private UUID testId;
    private IngredientDto testDto;
    private IngredientJpaEntity testEntity;
    private IngredientResponse testResponse;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

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
    }

    @Test
    void save_Success() {
        // Arrange
        when(mapper.toEntity(testDto)).thenReturn(testEntity);
        when(repository.save(testEntity)).thenReturn(testEntity);
        when(mapper.toDto(testEntity)).thenReturn(testDto);
        when(mapper.toResponse(testDto)).thenReturn(testResponse);

        // Act
        IngredientResponse result = adapter.save(testDto);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        assertEquals(testDto.getName(), result.getName());
        assertEquals(testDto.getDescription(), result.getDescription());
        assertEquals(testDto.getQuantity(), result.getQuantity());
        assertEquals(testDto.getUnit(), result.getUnit());
        assertEquals(testDto.getPrice(), result.getPrice());
        assertEquals(testDto.getSupplier(), result.getSupplier());
        assertEquals(testDto.getMinimumStock(), result.getMinimumStock());

        verify(mapper).toEntity(testDto);
        verify(repository).save(testEntity);
        verify(mapper).toDto(testEntity);
        verify(mapper).toResponse(testDto);
    }

    @Test
    void findById_Success() {
        // Arrange
        when(repository.findById(testId)).thenReturn(Optional.of(testEntity));
        when(mapper.toDto(testEntity)).thenReturn(testDto);
        when(mapper.toResponse(testDto)).thenReturn(testResponse);

        // Act
        Optional<IngredientResponse> result = adapter.findById(testId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testId, result.get().getId());
        assertEquals(testDto.getName(), result.get().getName());

        verify(repository).findById(testId);
        verify(mapper).toDto(testEntity);
        verify(mapper).toResponse(testDto);
    }

    @Test
    void findById_NotFound() {
        // Arrange
        when(repository.findById(testId)).thenReturn(Optional.empty());

        // Act
        Optional<IngredientResponse> result = adapter.findById(testId);

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findById(testId);
        verify(mapper, never()).toDto(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void findAll_Success() {
        // Arrange
        List<IngredientJpaEntity> entities = Arrays.asList(testEntity);
        when(repository.findAll()).thenReturn(entities);
        when(mapper.toDto(testEntity)).thenReturn(testDto);
        when(mapper.toResponse(testDto)).thenReturn(testResponse);

        // Act
        List<IngredientResponse> results = adapter.findAll();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testId, results.get(0).getId());
        assertEquals(testDto.getName(), results.get(0).getName());

        verify(repository).findAll();
        verify(mapper).toDto(testEntity);
        verify(mapper).toResponse(testDto);
    }

    @Test
    void deleteById_Success() {
        // Act
        adapter.deleteById(testId);

        // Assert
        verify(repository).deleteById(testId);
    }

    @Test
    void existsById_True() {
        // Arrange
        when(repository.existsById(testId)).thenReturn(true);

        // Act
        boolean result = adapter.existsById(testId);

        // Assert
        assertTrue(result);
        verify(repository).existsById(testId);
    }

    @Test
    void existsById_False() {
        // Arrange
        when(repository.existsById(testId)).thenReturn(false);

        // Act
        boolean result = adapter.existsById(testId);

        // Assert
        assertFalse(result);
        verify(repository).existsById(testId);
    }

    @Test
    void findBySupplier_Success() {
        // Arrange
        String supplier = "Proveedor A";
        List<IngredientJpaEntity> entities = Arrays.asList(testEntity);
        when(repository.findBySupplier(supplier)).thenReturn(entities);
        when(mapper.toDto(testEntity)).thenReturn(testDto);
        when(mapper.toResponse(testDto)).thenReturn(testResponse);

        // Act
        List<IngredientResponse> results = adapter.findBySupplier(supplier);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testId, results.get(0).getId());
        assertEquals(testDto.getName(), results.get(0).getName());
        assertEquals(supplier, results.get(0).getSupplier());

        verify(repository).findBySupplier(supplier);
        verify(mapper).toDto(testEntity);
        verify(mapper).toResponse(testDto);
    }
} 