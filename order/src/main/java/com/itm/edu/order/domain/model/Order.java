package com.itm.edu.order.domain.model;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.itm.edu.order.domain.valueobjects.OrderTotalValue;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    
    private LocalDateTime orderDate;
    private String orderStatus;
    private String orderNotes;
    
    @Embedded
    private AddressShipping deliveryAddress;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @Builder.Default
    private List<OrderItem> products = new ArrayList<>();

    @Embedded
    private OrderTotalValue total;

    public void setDeliveryAddress(AddressShipping deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @PrePersist
    @PreUpdate
    private void calculateTotal() {
        if (products != null && !products.isEmpty()) {
            BigDecimal totalAmount = products.stream()
                    .map(OrderItem::calculateValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            this.total = new OrderTotalValue(totalAmount, "USD");
        } else {
            this.total = new OrderTotalValue(BigDecimal.ZERO, "USD");
        }
    }

    public void addProduct(Product product, BigDecimal quantity) {
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .order(this)
                .build();
        products.add(orderItem);
        calculateTotal();
    }

    public void removeProduct(Product product) {
        products.removeIf(oi -> oi.getProduct().getId().equals(product.getId()));
        calculateTotal();
    }

    public BigDecimal calculateTotalValue() {
        return products.stream()
                .map(OrderItem::calculateValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
