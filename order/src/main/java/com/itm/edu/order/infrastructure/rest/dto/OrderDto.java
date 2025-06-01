package com.itm.edu.order.infrastructure.rest.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderDto {
    private UUID orderId;
    private ClientDto client;
    private List<OrderItemDto> items;
    private AddressDto shippingAddress;
    private MoneyDto total;
    private LocalDateTime orderDate;
    private String orderStatus;

    @Getter
    @Builder
    public static class ClientDto {
        private UUID id;
        private String name;
        private String email;
        private String phone;
    }

    @Getter
    @Builder
    public static class OrderItemDto {
        private UUID id;
        private ProductDto product;
        private int quantity;
    }

    @Getter
    @Builder
    public static class ProductDto {
        private UUID id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;
    }

    @Getter
    @Builder
    public static class AddressDto {
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }

    @Getter
    @Builder
    public static class MoneyDto {
        private BigDecimal amount;
        private String currency;
    }
} 