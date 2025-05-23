package com.itm.edu.stock.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.itm.edu.stock.domain.entities.Stock;
import com.itm.edu.stock.infrastructure.persistence.base.BaseJpaEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "stocks")
@Getter
@Setter
public class StockJpaEntity extends BaseJpaEntity<Stock> {
    
    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private IngredientJpaEntity ingredient;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private String unit;
    
    @Column(nullable = false)
    private BigDecimal price;

    public static StockJpaEntity fromDomain(Stock stock) {
        if (stock == null) return null;
        
        StockJpaEntity entity = new StockJpaEntity();
        entity.setId(stock.getId());
        entity.setIngredient(IngredientJpaEntity.fromDomain(stock.getIngredient()));
        entity.setQuantity(stock.getQuantity());
        entity.setUnit(stock.getUnit());
        entity.setPrice(stock.getPrice());
        return entity;
    }

    @Override
    public Stock toDomain() {
        return new Stock(
            this.id,
            this.ingredient.toDomain(),
            this.quantity,
            this.unit,
            this.price
        );
    }
} 