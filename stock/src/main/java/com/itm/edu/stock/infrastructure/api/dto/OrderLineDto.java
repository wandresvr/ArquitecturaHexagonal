package com.itm.edu.stock.infrastructure.api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderLineDto {
    private UUID productId;
    private BigDecimal quantity;
} 