package com.itm.edu.stock.infrastructure.mapper;

import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.domain.entities.Ingredient;
import org.springframework.stereotype.Component;

@Component
public class IngredientDomainMapper {
    
    public Ingredient toEntity(CreateIngredientCommand command) {
        return Ingredient.builder()
                .name(command.getName())
                .description(command.getDescription())
                .quantity(command.getQuantity())
                .unit(command.getUnit())
                .supplier(command.getSupplier())
                .minimumStock(command.getMinimumStock())
                .build();
    }
    
    public IngredientResponse toResponse(Ingredient ingredient) {
        return IngredientResponse.builder()
            .id(ingredient.getId())
            .name(ingredient.getName())
            .description(ingredient.getDescription())
            .quantity(ingredient.getQuantity())
            .unit(ingredient.getUnit())
            .supplier(ingredient.getSupplier())
            .minimumStock(ingredient.getMinimumStock())
            .price(ingredient.getPrice())
            .build();
    }
    
    public Ingredient updateEntityFromCommand(Ingredient ingredient, CreateIngredientCommand command) {
        return ingredient.toBuilder()
                .name(command.getName())
                .description(command.getDescription())
                .quantity(command.getQuantity())
                .unit(command.getUnit())
                .supplier(command.getSupplier())
                .minimumStock(command.getMinimumStock())
                .build();
    }

    public Ingredient toDomain(IngredientResponse response) {
        return Ingredient.builder()
            .id(response.getId())
            .name(response.getName())
            .description(response.getDescription())
            .quantity(response.getQuantity())
            .unit(response.getUnit())
            .supplier(response.getSupplier())
            .minimumStock(response.getMinimumStock())
            .price(response.getPrice())
            .build();
    }
} 