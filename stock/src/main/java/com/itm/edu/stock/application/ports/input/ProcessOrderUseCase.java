package com.itm.edu.stock.application.ports.input;

import com.itm.edu.common.dto.OrderMessageDTO;

/**
 * Interface for processing orders
 */
public interface ProcessOrderUseCase {
    void processOrder(OrderMessageDTO orderMessage);
}