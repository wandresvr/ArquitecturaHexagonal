package com.wilson.order.infrastructure.rest.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequestDto {
    private String customerName;
    private List<CreateOrderProductDto> products;
} 