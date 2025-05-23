package com.itm.edu.order.infrastructure.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderItemResponseDto {
    private UUID id;
    private ProductDto product;
    private int quantity;
} 