package com.wilson.stock.domain.entities;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;

import com.wilson.stock.domain.valueobjects.Quantity;
import com.wilson.stock.domain.valueobjects.Unit;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {
    @Id
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    
    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "quantity_value"))
    })
    private Quantity quantity;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "unit_value"))
    })
    private Unit unit;
} 