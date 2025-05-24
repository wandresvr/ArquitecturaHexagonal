package com.itm.edu.stock.infrastructure.persistence.mapper;

import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientResponse;
import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.domain.exception.BusinessException;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeDto;
import com.itm.edu.stock.infrastructure.persistence.dto.RecipeIngredientDto;
import com.itm.edu.stock.infrastructure.persistence.entity.RecipeJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.entity.RecipeIngredientJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RecipeMapper {
    private final IngredientPersistenceMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;
    
    public RecipeDto toDto(RecipeJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return RecipeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .instructions(entity.getInstructions())
                .preparationTime(entity.getPreparationTime())
                .difficulty(entity.getDifficulty())
                .recipeIngredients(
                    entity.getRecipeIngredients().stream()
                        .map(this::toRecipeIngredientDto)
                        .collect(Collectors.toList())
                )
                .cost(entity.getCost() != null ? entity.getCost() : BigDecimal.ZERO)
                .build();
    }

    private RecipeIngredientDto toRecipeIngredientDto(RecipeIngredientJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return RecipeIngredientDto.builder()
                .id(entity.getId())
                .recipeId(entity.getRecipe() != null ? entity.getRecipe().getId() : null)
                .ingredientId(entity.getIngredient() != null ? entity.getIngredient().getId() : null)
                .ingredientName(entity.getIngredient() != null ? entity.getIngredient().getName() : null)
                .quantity(entity.getQuantity())
                .unit(entity.getUnit())
                .build();
    }

    private RecipeIngredientJpaEntity toRecipeIngredientJpa(RecipeIngredientDto dto, RecipeJpaEntity recipe) {
        var ingredientResponse = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new BusinessException("Ingrediente no encontrado"));
        
        var entity = RecipeIngredientJpaEntity.builder()
                .id(dto.getId())
                .recipe(recipe)
                .ingredient(ingredientMapper.fromResponse(ingredientResponse))
                .quantity(dto.getQuantity())
                .unit(dto.getUnit() != null ? dto.getUnit() : ingredientResponse.getUnit())
                .build();
                
        return entity;
    }

    public RecipeJpaEntity toEntity(RecipeDto dto) {
        var entity = RecipeJpaEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .instructions(dto.getInstructions())
                .preparationTime(dto.getPreparationTime())
                .difficulty(dto.getDifficulty())
                .cost(dto.getCost())
                .build();

        entity.setRecipeIngredients(
            dto.getRecipeIngredients().stream()
                .map(ingredientDto -> toRecipeIngredientJpa(ingredientDto, entity))
                .collect(Collectors.toList())
        );

        return entity;
    }

    public RecipeResponse toResponse(RecipeDto dto) {
        if (dto == null) {
            return null;
        }
        return RecipeResponse.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .instructions(dto.getInstructions())
            .preparationTime(dto.getPreparationTime())
            .difficulty(dto.getDifficulty())
            .cost(dto.getCost() != null ? dto.getCost() : BigDecimal.ZERO)
            .ingredients(
                dto.getRecipeIngredients() != null ?
                    dto.getRecipeIngredients().stream()
                        .map(this::toIngredientResponse)
                        .collect(Collectors.toList()) :
                    null
            )
            .build();
    }

    private RecipeIngredientResponse toIngredientResponse(RecipeIngredientDto ri) {
        if (ri == null) {
            return null;
        }
        
        var ingredientName = ri.getIngredientName();
        if (ingredientName == null && ri.getIngredientId() != null) {
            ingredientName = ingredientRepository.findById(ri.getIngredientId())
                .map(ingredient -> ingredient.getName())
                .orElse(null);
        }
        
        return RecipeIngredientResponse.builder()
            .id(ri.getId())
            .recipeId(ri.getRecipeId())
            .ingredientId(ri.getIngredientId())
            .ingredientName(ingredientName)
            .quantity(ri.getQuantity())
            .unit(ri.getUnit())
            .build();
    }

    public RecipeDto fromCommand(CreateRecipeCommand command) {
        return fromCommand(command, UUID.randomUUID());
    }

    public RecipeDto fromCommand(CreateRecipeCommand command, UUID id) {
        if (command == null) {
            return null;
        }
        return RecipeDto.builder()
                .id(id)
                .name(command.getName())
                .description(command.getDescription())
                .instructions(command.getInstructions())
                .preparationTime(command.getPreparationTime())
                .difficulty(command.getDifficulty())
                .cost(BigDecimal.ZERO)
                .recipeIngredients(
                    command.getIngredients() != null ?
                        command.getIngredients().stream()
                            .map(ingredient -> RecipeIngredientDto.builder()
                                .id(UUID.randomUUID())
                                .recipeId(id)
                                .ingredientId(ingredient.getIngredientId())
                                .quantity(ingredient.getQuantity())
                                .unit(ingredient.getUnit())
                                .build())
                            .collect(Collectors.toList()) :
                        null
                )
                .build();
    }
} 