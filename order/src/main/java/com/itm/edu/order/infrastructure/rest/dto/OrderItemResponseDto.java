package com.itm.edu.order.infrastructure.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderItemResponseDto {
    private UUID id;
    private ProductDto product;
    private int quantity;
} 