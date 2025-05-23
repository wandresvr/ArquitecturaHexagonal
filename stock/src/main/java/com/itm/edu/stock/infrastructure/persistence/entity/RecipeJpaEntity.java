package com.itm.edu.stock.infrastructure.persistence.entity;

import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.infrastructure.persistence.base.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeJpaEntity extends BaseJpaEntity<Recipe> {
    @Id
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
    @Builder.Default
    private List<RecipeIngredientJpaEntity> recipeIngredients = new ArrayList<>();

    @Override
    public Recipe toDomain() {
        return Recipe.builder()
            .id(this.id)
            .name(this.name)
            .description(this.description)
            .instructions(this.instructions)
            .preparationTime(this.preparationTime)
            .difficulty(this.difficulty)
            .cost(this.cost)
            .build();
    }
} 