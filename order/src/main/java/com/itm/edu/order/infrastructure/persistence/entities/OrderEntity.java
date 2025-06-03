package com.itm.edu.order.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItemEntity> products = new ArrayList<>();

    private String orderStatus;
    private LocalDateTime orderDate;

    @Embedded
    private AddressShipping deliveryAddress;

    @Embedded
    private OrderTotalValue total;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    public void addProduct(OrderItemEntity product) {
        products.add(product);
        product.setOrder(this);
    }

    public void removeProduct(OrderItemEntity product) {
        products.remove(product);
        product.setOrder(null);
    }
} 