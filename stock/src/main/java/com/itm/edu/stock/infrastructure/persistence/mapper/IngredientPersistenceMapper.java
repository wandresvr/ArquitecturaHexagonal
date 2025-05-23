package com.itm.edu.stock.infrastructure.persistence.mapper;

import com.itm.edu.stock.application.dto.CreateIngredientCommand;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import com.itm.edu.stock.infrastructure.persistence.entity.IngredientJpaEntity;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component("ingredientPersistenceMapper")
@RequiredArgsConstructor
public class IngredientPersistenceMapper {
    
    public IngredientDto toDto(IngredientJpaEntity entity) {
        return IngredientDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .unit(entity.getUnit())
                .price(entity.getPrice())
                .supplier(entity.getSupplier())
                .minimumStock(entity.getMinimumStock())
                .build();
    }

    public IngredientJpaEntity toEntity(IngredientDto dto) {
        return IngredientJpaEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .quantity(dto.getQuantity())
                .unit(dto.getUnit())
                .price(dto.getPrice())
                .supplier(dto.getSupplier())
                .minimumStock(dto.getMinimumStock())
                .build();
    }

    public IngredientResponse toResponse(IngredientDto dto) {
        return IngredientResponse.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .quantity(dto.getQuantity())
            .unit(dto.getUnit())
            .supplier(dto.getSupplier())
            .minimumStock(dto.getMinimumStock())
            .price(dto.getPrice())
            .build();
    }

    public IngredientDto fromCommand(CreateIngredientCommand command) {
        return IngredientDto.builder()
                .name(command.getName())
                .description(command.getDescription())
                .quantity(command.getQuantity())
                .unit(command.getUnit())
                .supplier(command.getSupplier())
                .minimumStock(command.getMinimumStock())
                .build();
    }

    public IngredientJpaEntity fromResponse(IngredientResponse response) {
        return IngredientJpaEntity.builder()
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