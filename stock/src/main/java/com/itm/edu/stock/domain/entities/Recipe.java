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

    @ManyToMany
    @JoinTable(
        name = "recipe_ingredients",
        joinColumns = @JoinColumn(name = "recipe_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;
}
