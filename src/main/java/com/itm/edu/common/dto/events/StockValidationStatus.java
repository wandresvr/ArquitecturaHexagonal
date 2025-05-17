package com.itm.edu.common.dto.events;

public enum StockValidationStatus {
    RESERVED,                      // Stock reservado exitosamente
    CANCELLED_NO_STOCK,           // Cancelado por falta de stock o producto no encontrado
    UNAVAILABLE                   // Estado legacy, mantener por compatibilidad
} 