package com.itm.edu.stock.application.services;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.common.dto.ProductOrderDTO;
import com.itm.edu.stock.application.ports.input.ProcessOrderUseCase;
import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.entities.RecipeIngredient;
import com.itm.edu.stock.domain.exception.BusinessException;
import com.itm.edu.stock.domain.repository.IngredientRepository;
import com.itm.edu.stock.application.ports.output.RecipeRepository;
import com.itm.edu.stock.infrastructure.api.dto.OrderLineDto;
import com.itm.edu.stock.infrastructure.api.dto.ProcessOrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessOrderService implements ProcessOrderUseCase {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    @Transactional
    public void processOrder(OrderMessageDTO orderMessage) {
        for (ProductOrderDTO line : orderMessage.getProducts()) {
            Recipe recipe = recipeRepository
                .findById(line.getProductId())
                .orElseThrow(() ->
                    new BusinessException("Receta no encontrada para producto " + line.getProductId()));

            recipe.getRecipeIngredients().forEach(req -> {
                Ingredient ing = ingredientRepository
                    .findById(req.getIngredient().getId())
                    .orElseThrow(() ->
                        new BusinessException("Ingrediente no encontrado: " + req.getIngredient().getId()));

                BigDecimal needed = req.getQuantity().getValue()
                    .multiply(BigDecimal.valueOf(line.getQuantity()));

                if (ing.getQuantity().compareTo(needed) < 0) {
                    throw new BusinessException("Stock insuficiente de " + ing.getName());
                }

                ing.setQuantity(ing.getQuantity().subtract(needed));
                ingredientRepository.save(ing);
            });
        }
    }

    @Transactional
    public void processOrder(List<Ingredient> ingredients) {
        for (Ingredient req : ingredients) {
            Ingredient ing = ingredientRepository.findById(req.getId())
                    .orElseThrow(() -> 
                        new BusinessException("Ingrediente no encontrado: " + req.getId()));

            BigDecimal needed = req.getQuantity();
            if (ing.getQuantity().compareTo(needed) < 0) {
                throw new BusinessException("Stock insuficiente para el ingrediente: " + ing.getName());
            }

            ing.setQuantity(ing.getQuantity().subtract(needed));
            ingredientRepository.save(ing);
        }
    }

    @Transactional
    public void processOrder(ProcessOrderRequestDto request) {
        for (OrderLineDto line : request.getLines()) {
            Recipe recipe = recipeRepository.findById(line.getProductId())
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

            for (RecipeIngredient ri : recipe.getRecipeIngredients()) {
                Ingredient ingredient = ri.getIngredient();
                BigDecimal needed = ri.getQuantity().getValue().multiply(line.getQuantity());
                if (ingredient.getQuantity().compareTo(needed) < 0) {
                    throw new RuntimeException("No hay suficiente stock del ingrediente: " + ingredient.getName());
                }
                ingredient.setQuantity(ingredient.getQuantity().subtract(needed));
                ingredientRepository.save(ingredient);
            }
        }
    }
}
