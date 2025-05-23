package com.itm.edu.stock.application.services;

import com.itm.edu.stock.application.ports.input.IngredientDomainService;
import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class IngredientDomainServiceImpl implements IngredientDomainService {
    
    @Override
    public IngredientDto createIngredient(
        String name,
        String description,
        BigDecimal quantity,
        String unit,
        BigDecimal price,
        String supplier
    ) {
        return IngredientDto.builder()
            .id(UUID.randomUUID())
            .name(name)
            .description(description)
            .quantity(quantity)
            .unit(unit)
            .price(price)
            .supplier(supplier)
            .minimumStock(BigDecimal.ZERO) // Valor por defecto
            .build();
    }
} 