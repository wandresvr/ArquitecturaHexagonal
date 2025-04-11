package com.wilson.stock.domain.entities;

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

import com.wilson.stock.domain.valueobjects.Quantity;
import com.wilson.stock.domain.valueobjects.Unit;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    private UUID id;
    
    private String name;
    
    private String description;
    
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();
    
    public void addIngredient(Ingredient ingredient, BigDecimal quantity, String unit) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(UUID.randomUUID());
        recipeIngredient.setIngredient(ingredient);
        Quantity quantityObj = new Quantity();
        quantityObj.setValue(quantity);
        recipeIngredient.setQuantity(quantityObj);
        Unit unitObj = new Unit();
        unitObj.setValue(unit);
        recipeIngredient.setUnit(unitObj);
        ingredients.add(recipeIngredient);
    }
}
