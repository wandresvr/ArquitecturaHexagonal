package com.itm.edu.stock.application.ports.input;

import com.itm.edu.common.dto.OrderMessageDTO;

public interface ProcessOrderUseCase {
    /**
     * Valida una orden entrante (recetas + stock) y descuenta inventario.
     * Lanza BusinessException si falla.
     */
    void processOrder(OrderMessageDTO orderMessage);
}