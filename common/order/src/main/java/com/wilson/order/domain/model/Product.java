package com.wilson.order.domain.model;

import com.wilson.order.domain.exception.ProductValidationException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductValidationException("Price cannot be negative");
        }
        if (stock != null && stock < 0) {
            throw new ProductValidationException("Stock cannot be negative");
        }
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Integer getStock() { return stock; }
} 