package com.itm.edu.stock.infrastructure.persistence.adapter;

import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import com.itm.edu.stock.infrastructure.persistence.repository.IngredientJpaRepository;
import com.itm.edu.stock.infrastructure.persistence.mapper.IngredientPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class IngredientRepositoryAdapter implements IngredientRepository {
    private final IngredientJpaRepository repository;
    private final IngredientPersistenceMapper mapper;
    
    @Override
    public IngredientResponse save(IngredientDto dto) {
        var entity = mapper.toEntity(dto);
        var savedEntity = repository.save(entity);
        return mapper.toResponse(mapper.toDto(savedEntity));
    }
    
    @Override
    public Optional<IngredientResponse> findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .map(mapper::toResponse);
    }
    
    @Override
    public List<IngredientResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
    
    public List<IngredientResponse> findBySupplier(String supplier) {
        return repository.findBySupplier(supplier).stream()
                .map(mapper::toDto)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
} 