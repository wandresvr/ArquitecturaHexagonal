package com.itm.edu.common.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateResponseEvent {
    private UUID orderId;
    private StockValidationStatus status;
    private String reason; // Opcional: para indicar por qué falló (ej. qué producto faltó)
} 