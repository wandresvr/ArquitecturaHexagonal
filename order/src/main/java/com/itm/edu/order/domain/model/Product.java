package com.itm.edu.order.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Integer stock;

    private Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.stock = builder.stock;
    }

    public static Product create(String name, String description, BigDecimal price, Integer stock) {
        return builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();
    }

    public static Product reconstitute(UUID id, String name, String description, BigDecimal price, Integer stock) {
        return builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();
    }

    public Product withUpdatedDetails(String name, String description, BigDecimal price, Integer stock) {
        return builder()
                .id(this.id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stock;

        private Builder() {}

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
} 