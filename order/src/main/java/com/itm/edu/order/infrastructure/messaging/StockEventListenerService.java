package com.itm.edu.order.infrastructure.messaging;

import com.itm.edu.order.application.dto.events.StockUpdateResponseEvent;
import com.itm.edu.order.application.dto.events.StockValidationStatus;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.repository.OrderRepository;
import com.itm.edu.order.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListenerService {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.STOCK_RESPONSE_QUEUE)
    @Transactional
    public void handleStockUpdateResponse(StockUpdateResponseEvent event) {
        log.info("Received stock update response event: {}", event);

        if (event == null || event.getOrderId() == null || event.getStatus() == null) {
            log.error("Received invalid stock update event.");
            // Considerar enviar a una dead-letter queue
            return;
        }

        orderRepository.findById(event.getOrderId()).ifPresentOrElse(order -> {
            // Solo actualizamos si la orden está esperando validación
            if ("PENDING_VALIDATION".equals(order.getOrderStatus())) {
                String newStatus = event.getStatus() == StockValidationStatus.RESERVED
                        ? "APPROVED" // O tal vez "AWAITING_PAYMENT", "PROCESSING" etc.
                        : "CANCELLED_NO_STOCK";

                log.info("Updating order {} status from {} to {}", order.getOrderId(), order.getOrderStatus(), newStatus);
                order.setOrderStatus(newStatus);
                orderRepository.save(order);
                log.info("Order {} status updated successfully.", order.getOrderId());
            } else {
                log.warn("Received stock update for order {} which is not in PENDING_VALIDATION state (current state: {}). Ignoring event.",
                        order.getOrderId(), order.getOrderStatus());
            }
        }, () -> {
            log.error("Received stock update for non-existent order ID: {}", event.getOrderId());
            // No se puede hacer mucho aquí, la orden no existe
        });
    }
} 