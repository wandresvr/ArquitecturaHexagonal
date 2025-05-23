package com.itm.edu.stock.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.entities.RecipeIngredient;
import com.itm.edu.stock.infrastructure.persistence.base.BaseJpaEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "recipes")
@Getter
@Setter
public class RecipeJpaEntity extends BaseJpaEntity<Recipe> {
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 2000)
    private String instructions;

    @Column(name = "preparation_time")
    private Integer preparationTime;

    @Column(nullable = false)
    private String difficulty;

    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredientJpaEntity> recipeIngredients = new ArrayList<>();

    public static RecipeJpaEntity fromDomain(Recipe recipe) {
        if (recipe == null) return null;
        
        RecipeJpaEntity entity = new RecipeJpaEntity();
        entity.setId(recipe.getId());
        entity.setName(recipe.getName());
        entity.setDescription(recipe.getDescription());
        entity.setInstructions(recipe.getInstructions());
        entity.setPreparationTime(recipe.getPreparationTime());
        entity.setDifficulty(recipe.getDifficulty());
        entity.setCost(recipe.getCost());
        
        if (recipe.getRecipeIngredients() != null) {
            entity.setRecipeIngredients(
                recipe.getRecipeIngredients().stream()
                    .map(RecipeIngredientJpaEntity::fromDomain)
                    .collect(Collectors.toList())
            );
        }
        return entity;
    }

    @Override
    public Recipe toDomain() {
        return new Recipe(
            this.id,
            this.name,
            this.description,
            this.instructions,
            this.preparationTime,
            this.difficulty,
            this.cost,
            this.recipeIngredients.stream()
                .map(RecipeIngredientJpaEntity::toDomain)
                .collect(Collectors.toList())
        );
    }
} 