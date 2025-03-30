package com.wilson.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.wilson.order.domain.valueobjects.AddressShipping;
import com.wilson.order.domain.valueobjects.OrderTotal;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private UUID orderId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @Builder.Default
    private List<OrderTotal> products = new ArrayList<>();

    private String orderStatus;
    private LocalDateTime orderDate;
    private String orderNotes;
    
    @Embedded
    private AddressShipping deliveryAddress;


    // Business methods
    public void addProduct(Product product, BigDecimal quantity) {
        OrderTotal orderTotal = OrderTotal.builder()
                .product(product)
                .quantity(quantity)
                .build();
        products.add(orderTotal);
    }

    public void removeProduct(Product product) {
        products.removeIf(oi -> oi.getProduct().getId().equals(product.getId()));
    }

    public BigDecimal calculateTotalValue() {
        return products.stream()
                .map(OrderTotal::calculateValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
