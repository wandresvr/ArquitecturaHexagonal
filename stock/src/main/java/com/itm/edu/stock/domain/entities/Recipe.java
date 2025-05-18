package com.itm.edu.stock.domain.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

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
    private List<RecipeIngredient> recipeIngredients;
}
