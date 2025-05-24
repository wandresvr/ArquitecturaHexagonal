package com.itm.edu.stock.infrastructure.api.mapper;

import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Component("ingredientApiMapper")
@RequiredArgsConstructor
public class IngredientApiMapper {
    
    public IngredientResponseDto toResponseDto(IngredientResponse response) {
        return new IngredientResponseDto(
            response.getId(),
            response.getName(),
            response.getDescription(),
            response.getQuantity(),
            response.getUnit(),
            response.getSupplier(),
            response.getMinimumStock()
        );
    }

    public CreateIngredientCommand toCommand(CreateIngredientRequestDto dto) {
        return new CreateIngredientCommand(
            dto.getName(),
            dto.getDescription(),
            dto.getQuantity(),
            dto.getUnit(),
            dto.getSupplier(),
            BigDecimal.ZERO,
            dto.getPrice()
        );
    }
} 