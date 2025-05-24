package com.itm.edu.stock.infrastructure.persistence.adapter;

import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeDto;
import com.itm.edu.stock.infrastructure.persistence.mapper.RecipeMapper;
import com.itm.edu.stock.infrastructure.persistence.repository.RecipeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecipeRepositoryAdapter implements RecipeRepository {
    private final RecipeJpaRepository jpaRepository;
    private final RecipeMapper mapper;

    @Override
    public RecipeResponse save(RecipeDto recipe) {
        var entity = mapper.toEntity(recipe);
        
        // Si la entidad ya existe, cargar sus relaciones existentes
        if (jpaRepository.existsById(entity.getId())) {
            var existingEntity = jpaRepository.findById(entity.getId())
                    .orElseThrow(() -> new RuntimeException("Recipe not found"));
            entity.getRecipeIngredients().forEach(ri -> ri.setRecipe(entity));
        }
        
        var savedEntity = jpaRepository.save(entity);
        return mapper.toResponse(mapper.toDto(savedEntity));
    }

    @Override
    public Optional<RecipeResponse> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDto)
                .map(mapper::toResponse);
    }

    @Override
    public List<RecipeResponse> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDto)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<RecipeResponse> findByDifficulty(String difficulty) {
        return jpaRepository.findByDifficulty(difficulty).stream()
                .map(mapper::toDto)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
} 