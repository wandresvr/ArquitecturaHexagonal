package com.itm.edu.stock.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.itm.edu.stock.domain.entities.RecipeIngredient;
import com.itm.edu.stock.domain.valueobjects.Quantity;
import com.itm.edu.stock.domain.valueobjects.Unit;
import com.itm.edu.stock.infrastructure.persistence.base.BaseJpaEntity;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
public class RecipeIngredientJpaEntity extends BaseJpaEntity<RecipeIngredient> {
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeJpaEntity recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private IngredientJpaEntity ingredient;

    @Embedded
    private Quantity quantity;

    @Embedded
    private Unit unit;

    public static RecipeIngredientJpaEntity fromDomain(RecipeIngredient recipeIngredient) {
        if (recipeIngredient == null) return null;
        
        RecipeIngredientJpaEntity entity = new RecipeIngredientJpaEntity();
        entity.setId(recipeIngredient.getId());
        entity.setRecipe(RecipeJpaEntity.fromDomain(recipeIngredient.getRecipe()));
        entity.setIngredient(IngredientJpaEntity.fromDomain(recipeIngredient.getIngredient()));
        entity.setQuantity(recipeIngredient.getQuantity());
        entity.setUnit(recipeIngredient.getUnit());
        return entity;
    }

    @Override
    public RecipeIngredient toDomain() {
        return new RecipeIngredient(
            this.id,
            this.recipe.toDomain(),
            this.ingredient.toDomain(),
            this.quantity,
            this.unit
        );
    }
} 