package com.itm.edu.stock.infrastructure.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProcessOrderRequestDto {
    private List<OrderLineDto> lines;
} 