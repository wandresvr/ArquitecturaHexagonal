package com.itm.edu.stock.application.ports.input;

import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import java.math.BigDecimal;

public interface IngredientDomainService {
    IngredientDto createIngredient(
        String name,
        String description,
        BigDecimal quantity,
        String unit,
        BigDecimal price,
        String supplier
    );
} 