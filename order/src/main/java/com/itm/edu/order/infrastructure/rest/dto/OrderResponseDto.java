package com.itm.edu.order.infrastructure.rest.dto;

import com.itm.edu.order.domain.model.Order;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderResponseDto {
    private UUID orderId;
    private ClientResponseDto client;
    private List<OrderItemResponseDto> products;
    private AddressResponseDto deliveryAddress;
    private MoneyResponseDto total;
    private LocalDateTime orderDate;
    private String orderStatus;

    @Data
    @Builder
    public static class ClientResponseDto {
        private UUID id;
        private String name;
        private String email;
        private String phone;
    }

    @Data
    @Builder
    public static class OrderItemResponseDto {
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

    @Data
    @Builder
    public static class AddressResponseDto {
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }

    @Data
    @Builder
    public static class MoneyResponseDto {
        private BigDecimal amount;
        private String currency;
    }

    public static OrderResponseDto fromDomain(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .client(ClientResponseDto.builder()
                        .id(order.getClient().getId())
                        .name(order.getClient().getName())
                        .email(order.getClient().getEmail())
                        .phone(order.getClient().getPhone())
                        .build())
                .products(order.getProducts().stream()
                        .map(item -> OrderItemResponseDto.builder()
                                .quantity(item.getQuantity())
                                .product(item.getProduct() != null ? ProductResponseDto.builder()
                                        .id(item.getProduct().getId())
                                        .name(item.getProduct().getName())
                                        .description(item.getProduct().getDescription())
                                        .price(item.getProduct().getPrice().doubleValue())
                                        .stock(item.getProduct().getStock())
                                        .build() : null)
                                .build())
                        .collect(Collectors.toList()))
                .deliveryAddress(AddressResponseDto.builder()
                        .street(order.getDeliveryAddress().getStreet())
                        .city(order.getDeliveryAddress().getCity())
                        .state(order.getDeliveryAddress().getState())
                        .zipCode(order.getDeliveryAddress().getZipCode())
                        .country(order.getDeliveryAddress().getCountry())
                        .build())
                .total(MoneyResponseDto.builder()
                        .amount(order.getTotal().getAmount())
                        .currency(order.getTotal().getCurrency())
                        .build())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .build();
    }
} 