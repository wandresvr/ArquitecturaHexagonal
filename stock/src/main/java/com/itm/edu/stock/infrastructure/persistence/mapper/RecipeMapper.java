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

import java.util.stream.Collectors;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RecipeMapper {
    private final IngredientPersistenceMapper ingredientMapper;
    private final IngredientRepository ingredientRepository;
    
    public RecipeDto toDto(RecipeJpaEntity entity) {
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
                .cost(entity.getCost())
                .build();
    }

    private RecipeIngredientDto toRecipeIngredientDto(RecipeIngredientJpaEntity entity) {
        return RecipeIngredientDto.builder()
                .id(entity.getId())
                .recipeId(entity.getRecipe().getId())
                .ingredientId(entity.getIngredient().getId())
                .ingredientName(entity.getIngredient().getName())
                .quantity(entity.getQuantity())
                .unit(entity.getUnit())
                .build();
    }

    private RecipeIngredientJpaEntity toRecipeIngredientJpa(RecipeIngredientDto dto, RecipeJpaEntity recipe) {
        var ingredientResponse = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new BusinessException("Ingrediente no encontrado"));
        
        return RecipeIngredientJpaEntity.builder()
                .id(dto.getId())
                .recipe(recipe)
                .ingredient(ingredientMapper.fromResponse(ingredientResponse))
                .quantity(dto.getQuantity())
                .unit(dto.getUnit())
                .build();
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
        return RecipeResponse.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .instructions(dto.getInstructions())
            .preparationTime(dto.getPreparationTime())
            .difficulty(dto.getDifficulty())
            .cost(dto.getCost())
            .ingredients(
                dto.getRecipeIngredients().stream()
                    .map(this::toIngredientResponse)
                    .collect(Collectors.toList())
            )
            .build();
    }

    public RecipeDto fromCommand(CreateRecipeCommand command) {
        var recipeId = UUID.randomUUID();
        return RecipeDto.builder()
                .id(recipeId)
                .name(command.getName())
                .description(command.getDescription())
                .instructions(command.getInstructions())
                .preparationTime(command.getPreparationTime())
                .difficulty(command.getDifficulty())
                .recipeIngredients(
                    command.getIngredients().stream()
                        .map(ingredient -> RecipeIngredientDto.builder()
                            .id(UUID.randomUUID())
                            .recipeId(recipeId)
                            .ingredientId(ingredient.getIngredientId())
                            .quantity(ingredient.getQuantity())
                            .unit(ingredient.getUnit())
                            .build())
                        .collect(Collectors.toList())
                )
                .build();
    }

    private RecipeIngredientResponse toIngredientResponse(RecipeIngredientDto ri) {
        return RecipeIngredientResponse.builder()
            .id(ri.getId())
            .recipeId(ri.getRecipeId())
            .ingredientId(ri.getIngredientId())
            .ingredientName(ri.getIngredientName())
            .quantity(ri.getQuantity())
            .unit(ri.getUnit())
            .build();
    }
} 