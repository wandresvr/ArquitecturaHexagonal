package com.itm.edu.stock.infrastructure.api.mapper;

import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    public IngredientResponseDto toDto(Ingredient ingredient) {
        IngredientResponseDto dto = new IngredientResponseDto();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setDescription(ingredient.getDescription());
        dto.setQuantity(ingredient.getQuantity());
        dto.setUnit(ingredient.getUnit());
        dto.setPrice(ingredient.getPrice());
        dto.setSupplier(ingredient.getSupplier());
        return dto;
    }

    public Ingredient toEntity(CreateIngredientRequestDto dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        ingredient.setDescription(dto.getDescription());
        ingredient.setQuantity(dto.getQuantity());
        ingredient.setUnit(dto.getUnit());
        ingredient.setPrice(dto.getPrice());
        ingredient.setSupplier(dto.getSupplier());
        return ingredient;
    }
} 