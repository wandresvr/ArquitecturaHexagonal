package com.itm.edu.stock.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private String unit;
    
    @Column(nullable = false)
    private Double price;
} 