package com.wilson.order.domain.valueobjects;

import com.wilson.order.domain.model.Order;
import com.wilson.order.domain.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_totals")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTotal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    private BigDecimal quantity;    // Changed to BigDecimal for precise measurements
    
    public BigDecimal calculateValue() {
        return product.getPrice().multiply(quantity);
    }
} 