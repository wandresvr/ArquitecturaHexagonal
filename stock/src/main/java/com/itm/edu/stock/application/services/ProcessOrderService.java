package com.itm.edu.stock.application.services;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.common.dto.ProductOrderDTO;
import com.itm.edu.stock.application.ports.input.ProcessOrderUseCase;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.domain.exception.BusinessException;
import com.itm.edu.stock.domain.repository.RecipeRepository;
import com.itm.edu.stock.domain.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProcessOrderService implements ProcessOrderUseCase {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    @Transactional
    public void processOrder(OrderMessageDTO orderMessage) {
        for (ProductOrderDTO line : orderMessage.getProducts()) {
            // 1) Recupera la receta según productId
            Recipe recipe = recipeRepository
                .findByProductId(line.getProductId())
                .orElseThrow(() ->
                    new BusinessException("Receta no encontrada para producto " + line.getProductId()));

            // 2) Valida y descuenta ingredientes
            recipe.getIngredients().forEach(req -> {
                Ingredient ing = ingredientRepository
                    .findById(req.getIngredient().getId())
                    .orElseThrow(() ->
                        new BusinessException("Ingrediente no encontrado: " + req.getIngredient().getId()));
                BigDecimal needed = req.getQuantity().getValue()
                    .multiply(BigDecimal.valueOf(line.getQuantity()));
                if (ing.getQuantity().isLessThan(needed)) {
                    throw new BusinessException("Stock insuficiente de " + ing.getName());
                }
                ing.setQuantity(ing.getQuantity().subtract(needed));
                ingredientRepository.save(ing);
            });
        }
    }
}

