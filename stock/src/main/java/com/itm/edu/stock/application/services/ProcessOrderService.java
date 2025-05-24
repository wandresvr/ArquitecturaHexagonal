package com.itm.edu.stock.application.services;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.common.dto.ProductOrderDTO;
import com.itm.edu.stock.application.ports.input.ProcessOrderUseCase;
import com.itm.edu.stock.application.ports.output.IngredientRepository;
import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.application.dto.RecipeResponse;
import com.itm.edu.stock.application.dto.IngredientResponse;
import com.itm.edu.stock.infrastructure.persistence.dto.IngredientDto;
import com.itm.edu.stock.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessOrderService implements ProcessOrderUseCase {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    @Override
    @Transactional
    public void processOrder(OrderMessageDTO orderMessage) {
        for (ProductOrderDTO product : orderMessage.getProducts()) {
            RecipeResponse recipe = recipeRepository.findById(product.getProductId())
                    .orElseThrow(() -> new BusinessException("Receta no encontrada"));

            for (var recipeIngredient : recipe.getIngredients()) {
                IngredientResponse ingredient = ingredientRepository.findById(recipeIngredient.getIngredientId())
                        .orElseThrow(() -> new BusinessException("Ingrediente no encontrado: " + recipeIngredient.getIngredientName()));

                if (recipeIngredient.getUnit() == null) {
                    throw new BusinessException("La unidad no está especificada en la receta para el ingrediente: " + ingredient.getName());
                }
                if (ingredient.getUnit() == null) {
                    throw new BusinessException("La unidad no está especificada en el ingrediente: " + ingredient.getName());
                }
                if (!recipeIngredient.getUnit().equals(ingredient.getUnit())) {
                    throw new BusinessException("Las unidades no coinciden para el ingrediente " + ingredient.getName() + 
                        ". Receta: " + recipeIngredient.getUnit() + ", Ingrediente: " + ingredient.getUnit());
                }

                BigDecimal requiredQuantity = recipeIngredient.getQuantity()
                    .multiply(BigDecimal.valueOf(product.getQuantity()));

                if (ingredient.getQuantity().compareTo(requiredQuantity) < 0) {
                    throw new BusinessException("Stock insuficiente para el ingrediente: " + ingredient.getName() + 
                        ". Requerido: " + requiredQuantity + " " + ingredient.getUnit() + 
                        ", Disponible: " + ingredient.getQuantity() + " " + ingredient.getUnit());
                }

                IngredientDto updatedIngredient = IngredientDto.builder()
                    .id(ingredient.getId())
                    .name(ingredient.getName())
                    .description(ingredient.getDescription())
                    .quantity(ingredient.getQuantity().subtract(requiredQuantity))
                    .unit(ingredient.getUnit())
                    .price(ingredient.getPrice())
                    .supplier(ingredient.getSupplier())
                    .minimumStock(ingredient.getMinimumStock())
                    .build();

                ingredientRepository.save(updatedIngredient);
            }
        }
    }
}
