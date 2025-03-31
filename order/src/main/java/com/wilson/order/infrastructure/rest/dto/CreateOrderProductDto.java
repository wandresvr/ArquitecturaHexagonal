package com.wilson.order.infrastructure.rest.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateOrderProductDto {
    private UUID productId;
    private BigDecimal quantity;
} 