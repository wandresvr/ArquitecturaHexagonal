package com.itm.edu.stock.infrastructure.api.mapper;

import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.infrastructure.api.dto.CreateIngredientRequestDto;
import com.itm.edu.stock.infrastructure.api.dto.IngredientResponseDto;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class IngredientApiMapper {
    
    public CreateIngredientCommand toCommand(CreateIngredientRequestDto request) {
        if (request == null) {
            return null;
        }
        return new CreateIngredientCommand(
            request.getName(),
            request.getDescription(),
            request.getQuantity(),
            request.getUnit(),
            request.getSupplier(),
            BigDecimal.ZERO,
            request.getPrice()
        );
    }
    
    public IngredientResponseDto toResponseDto(IngredientResponse response) {
        if (response == null) {
            return null;
        }
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
} 