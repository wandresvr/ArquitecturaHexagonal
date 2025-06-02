package com.itm.edu.order.infrastructure.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ProductDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
} 