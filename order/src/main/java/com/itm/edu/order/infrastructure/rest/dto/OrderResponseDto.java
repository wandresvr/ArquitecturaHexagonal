package com.itm.edu.order.infrastructure.rest.dto;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderResponseDto {
    private UUID orderId;
    private Client client;
    private List<OrderItem> products;
    private AddressShipping deliveryAddress;
    private OrderTotalValue total;
    private LocalDateTime orderDate;
    private String orderStatus;

    @Data
    @Builder
    public static class OrderItemResponseDto {
        private UUID id;
        private ProductResponseDto product;
        private int quantity;
    }

    @Data
    @Builder
    public static class ProductResponseDto {
        private UUID id;
        private String name;
        private String description;
        private double price;
        private int stock;
    }

    public static OrderResponseDto fromDomain(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .client(order.getClient())
                .products(order.getProducts())
                .deliveryAddress(order.getDeliveryAddress())
                .total(order.getTotal())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .build();
    }
} 