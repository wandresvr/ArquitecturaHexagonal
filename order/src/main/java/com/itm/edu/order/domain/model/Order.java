package com.itm.edu.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @Builder.Default
    @JsonManagedReference
    private List<OrderItem> products = new ArrayList<>();

    private String orderStatus;
    private LocalDateTime orderDate;
    //private String orderNotes;
    
    @Embedded
    private AddressShipping deliveryAddress;

    @Embedded
    @Builder.Default
    private OrderTotalValue total = OrderTotalValue.zero();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public void setDeliveryAddress(AddressShipping deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @PrePersist
    @PreUpdate
    protected void calculateTotal() {
        if (products != null && !products.isEmpty()) {
            BigDecimal totalAmount = products.stream()
                    .map(OrderItem::calculateValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            this.total = new OrderTotalValue(totalAmount, "USD");
        } else {
            this.total = new OrderTotalValue(BigDecimal.ZERO, "USD");
        }
    }

    // Business methods
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
}
