package com.itm.edu.stock.domain.entities;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.UUID;
import com.itm.edu.stock.domain.valueobjects.Quantity;
import com.itm.edu.stock.domain.valueobjects.Unit;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    
    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
    
    @Embedded
    private Quantity quantity;
    
    @Embedded
    private Unit unit;
} 