package com.itm.edu.common.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateResponseEvent {
    private String orderId;
    private StockValidationStatus status;
} 