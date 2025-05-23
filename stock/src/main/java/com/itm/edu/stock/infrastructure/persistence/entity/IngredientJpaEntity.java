package com.itm.edu.stock.infrastructure.persistence.entity;

import com.itm.edu.stock.domain.entities.Ingredient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientJpaEntity {
    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal price;
    private String supplier;
    private BigDecimal minimumStock;

    public static IngredientJpaEntity fromDomain(Ingredient ingredient) {
        return IngredientJpaEntity.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .description(ingredient.getDescription())
                .quantity(ingredient.getQuantity())
                .unit(ingredient.getUnit())
                .supplier(ingredient.getSupplier())
                .minimumStock(ingredient.getMinimumStock())
                .build();
    }

    public Ingredient toDomain() {
        return Ingredient.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .quantity(this.quantity)
                .unit(this.unit)
                .supplier(this.supplier)
                .minimumStock(this.minimumStock)
                .build();
    }
} 