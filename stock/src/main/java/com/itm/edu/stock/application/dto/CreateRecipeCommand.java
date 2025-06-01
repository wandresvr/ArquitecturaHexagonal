package com.itm.edu.stock.application.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateRecipeCommand {
    String name;
    String description;
    String instructions;
    Integer preparationTime;
    String difficulty;
    List<RecipeIngredientCommand> ingredients;
} 