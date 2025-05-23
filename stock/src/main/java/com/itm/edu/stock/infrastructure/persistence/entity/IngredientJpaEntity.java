package com.itm.edu.stock.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.infrastructure.persistence.base.BaseJpaEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
public class IngredientJpaEntity extends BaseJpaEntity<Ingredient> {
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String supplier;

    public static IngredientJpaEntity fromDomain(Ingredient ingredient) {
        if (ingredient == null) return null;
        
        IngredientJpaEntity entity = new IngredientJpaEntity();
        entity.setId(ingredient.getId());
        entity.setName(ingredient.getName());
        entity.setDescription(ingredient.getDescription());
        entity.setQuantity(ingredient.getQuantity());
        entity.setUnit(ingredient.getUnit());
        entity.setPrice(ingredient.getPrice());
        entity.setSupplier(ingredient.getSupplier());
        return entity;
    }

    @Override
    public Ingredient toDomain() {
        return new Ingredient(
            this.id,
            this.name,
            this.description,
            this.quantity,
            this.unit,
            this.price,
            this.supplier
        );
    }
} 