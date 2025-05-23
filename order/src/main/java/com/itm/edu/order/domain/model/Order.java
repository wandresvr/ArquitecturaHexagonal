package com.itm.edu.order.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;

@Getter
@Builder
public class Order {
    private final UUID orderId;
    private final Client client;
    private String orderStatus;
    private final LocalDateTime orderDate;
    private final AddressShipping deliveryAddress;
    private final OrderTotalValue total;
    @Builder.Default
    private final List<OrderItem> products = new ArrayList<>();

    public void updateStatus(String newStatus) {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de la orden no puede ser nulo o vacío");
        }
        this.orderStatus = newStatus;
    }

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        OrderItem orderItem = OrderItem.builder()
            .product(product)
            .quantity(quantity)
            .build();
            
        this.products.add(orderItem);
    }

    public static Order create(Client client, List<OrderItem> products, AddressShipping deliveryAddress) {
        return Order.builder()
            .orderId(UUID.randomUUID())
            .client(client)
            .products(products)
            .deliveryAddress(deliveryAddress)
            .orderDate(LocalDateTime.now())
            .orderStatus("PENDING_VALIDATION")
            .total(calculateTotalFromProducts(products))
            .build();
    }

    public Order withUpdatedStatus(String newStatus) {
        return Order.builder()
            .orderId(this.orderId)
            .client(this.client)
            .products(this.products)
            .deliveryAddress(this.deliveryAddress)
            .orderDate(this.orderDate)
            .orderStatus(newStatus)
            .total(this.total)
            .build();
    }

    public Order withUpdatedClient(Client newClient) {
        return Order.builder()
            .orderId(this.orderId)
            .client(newClient)
            .products(this.products)
            .deliveryAddress(this.deliveryAddress)
            .orderDate(this.orderDate)
            .orderStatus(this.orderStatus)
            .total(this.total)
            .build();
    }

    public Order withUpdatedDeliveryAddress(AddressShipping newAddress) {
        return Order.builder()
            .orderId(this.orderId)
            .client(this.client)
            .products(this.products)
            .deliveryAddress(newAddress)
            .orderDate(this.orderDate)
            .orderStatus(this.orderStatus)
            .total(this.total)
            .build();
    }

    public Order withAddedProduct(Product product, int quantity) {
        List<OrderItem> newProducts = new ArrayList<>(this.products);
        newProducts.add(OrderItem.builder()
            .product(product)
            .quantity(quantity)
            .build());
        
        return Order.builder()
            .orderId(this.orderId)
            .client(this.client)
            .products(newProducts)
            .deliveryAddress(this.deliveryAddress)
            .orderDate(this.orderDate)
            .orderStatus(this.orderStatus)
            .total(calculateTotalFromProducts(newProducts))
            .build();
    }

    public Order withRemovedProduct(Product product) {
        List<OrderItem> newProducts = new ArrayList<>(this.products);
        newProducts.removeIf(item -> item.getProduct().getId().equals(product.getId()));
        
        return Order.builder()
            .orderId(this.orderId)
            .client(this.client)
            .products(newProducts)
            .deliveryAddress(this.deliveryAddress)
            .orderDate(this.orderDate)
            .orderStatus(this.orderStatus)
            .total(calculateTotalFromProducts(newProducts))
            .build();
    }

    private static OrderTotalValue calculateTotalFromProducts(List<OrderItem> products) {
        if (products == null || products.isEmpty()) {
            return new OrderTotalValue(BigDecimal.ZERO, "USD");
        }
        
        BigDecimal totalAmount = products.stream()
            .map(OrderItem::calculateValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new OrderTotalValue(totalAmount, "USD");
    }
}
