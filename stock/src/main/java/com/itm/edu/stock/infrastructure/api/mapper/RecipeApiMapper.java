package com.itm.edu.stock.infrastructure.api.mapper;

import com.itm.edu.stock.application.dto.CreateRecipeCommand;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.RecipeIngredientCommand;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.RecipeResponseDto;
import com.itm.edu.stock.infrastructure.api.dto.CreateRecipeIngredientDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import com.itm.edu.stock.infrastructure.api.dto.UpdateRecipeRequestDto;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import java.util.UUID;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class RecipeApiMapper {
    
    public CreateRecipeCommand toCommand(CreateRecipeRequestDto request) {
        return new CreateRecipeCommand(
            request.getName(),
            request.getDescription(),
            request.getInstructions(),
            request.getPreparationTime(),
            request.getDifficulty(),
            request.getIngredients().stream()
                .map(this::toIngredientCommand)
                .collect(Collectors.toList())
        );
    }

    public CreateRecipeCommand toCommand(UpdateRecipeRequestDto request) {
        return new CreateRecipeCommand(
            request.getName(),
            request.getDescription(),
            request.getInstructions(),
            request.getPreparationTime(),
            request.getDifficulty(),
            request.getIngredients() != null ?
                request.getIngredients().stream()
                    .map(this::toIngredientCommand)
                    .collect(Collectors.toList()) :
                null
        );
    }

    private RecipeIngredientCommand toIngredientCommand(CreateRecipeIngredientDto dto) {
        return new RecipeIngredientCommand(
            dto.getIngredientId(),
            dto.getQuantity(),
            dto.getUnit()
        );
    }

    public RecipeResponseDto toDto(RecipeResponse response) {
        var dto = new RecipeResponseDto();
        dto.setId(response.getId());
        dto.setName(response.getName());
        dto.setDescription(response.getDescription());
        dto.setInstructions(response.getInstructions());
        dto.setPreparationTime(response.getPreparationTime());
        dto.setDifficulty(response.getDifficulty());
        dto.setCost(response.getCost());
        dto.setIngredients(response.getIngredients().stream()
            .map(ingredient -> new IngredientResponseDto(
                ingredient.getIngredientId(),
                ingredient.getIngredientName(),
                "",  // description
                ingredient.getQuantity(),
                ingredient.getUnit(),
                "",  // supplier
                null  // minimumStock
            ))
            .collect(Collectors.toList()));
        return dto;
    }
} 