package com.itm.edu.stock.application.dto;

import lombok.Value;
import java.util.List;

@Value
public class CreateRecipeCommand {
    String name;
    String description;
    String instructions;
    Integer preparationTime;
    String difficulty;
    List<RecipeIngredientCommand> ingredients;
} 