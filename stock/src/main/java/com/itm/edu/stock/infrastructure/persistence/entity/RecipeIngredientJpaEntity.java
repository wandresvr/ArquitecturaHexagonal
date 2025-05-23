package com.itm.edu.stock.infrastructure.persistence.entity;

import com.itm.edu.stock.domain.entities.RecipeIngredient;
import com.itm.edu.stock.infrastructure.persistence.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientJpaEntity extends BaseJpaEntity<RecipeIngredient> {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private RecipeJpaEntity recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private IngredientJpaEntity ingredient;

    private BigDecimal quantity;
    private String unit;

    @Override
    public RecipeIngredient toDomain() {
        return RecipeIngredient.builder()
            .id(this.id)
            .recipeId(this.recipe != null ? this.recipe.getId() : null)
            .ingredientId(this.ingredient != null ? this.ingredient.getId() : null)
            .ingredientName(this.ingredient != null ? this.ingredient.getName() : null)
            .quantity(this.quantity)
            .unit(this.unit)
            .build();
    }
} 