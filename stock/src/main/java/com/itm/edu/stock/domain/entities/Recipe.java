package com.itm.edu.stock.domain.entities;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;

import com.itm.edu.stock.domain.valueobjects.Quantity;
import com.itm.edu.stock.domain.valueobjects.Unit;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    private UUID id;
    
    private UUID productId;
    
    private String name;
    
    private String description;
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();
    
    public void addIngredient(Ingredient ingredient, BigDecimal quantity, String unit) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(UUID.randomUUID());
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(new Quantity(quantity));
        recipeIngredient.setUnit(new Unit(unit));
        recipeIngredient.setRecipe(this);
        ingredients.add(recipeIngredient);
    }
}
