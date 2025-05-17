package com.itm.edu.order.infrastructure.messaging;

import com.itm.edu.order.application.dto.events.StockUpdateResponseEvent;
import com.itm.edu.order.application.dto.events.StockValidationStatus;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.repository.OrderRepository;
import com.itm.edu.order.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListenerService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.STOCK_RESPONSE_QUEUE)
    @Transactional
    public void handleStockUpdateResponse(StockUpdateResponseEvent event) {
        log.info("Received stock update response event: {}", event);

        if (event == null || event.getOrderId() == null || event.getStatus() == null) {
            log.error("Received invalid stock update event.");
            return;
        }

        orderRepository.findById(event.getOrderId()).ifPresentOrElse(order -> {
            // Solo actualizamos si la orden está esperando validación
            if ("PENDING_VALIDATION".equals(order.getOrderStatus())) {
                String newStatus;
                
                if (event.getStatus() == StockValidationStatus.RESERVED) {
                    newStatus = "APPROVED";
                    // Enviar mensaje para actualizar el inventario
                    sendStockUpdateMessage(order);
                } else {
                    newStatus = "CANCELLED_NO_STOCK";
                }

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
        });
    }

    private void sendStockUpdateMessage(Order order) {
        try {
            // Convertir OrderItems a ProductUpdates
            List<ProductUpdate> productUpdates = order.getProducts().stream()
                .map(item -> new ProductUpdate(
                    item.getProduct().getId().toString(),
                    item.getQuantity().intValue()
                ))
                .collect(Collectors.toList());

            // Crear mensaje de actualización de inventario
            StockUpdateMessage updateMessage = new StockUpdateMessage(
                order.getOrderId().toString(),
                productUpdates
            );

            // Enviar al exchange de actualización de inventario
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.STOCK_UPDATE_EXCHANGE,
                RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY,
                updateMessage
            );

            log.info("Stock update message sent for order: {}", order.getOrderId());
        } catch (Exception e) {
            log.error("Error sending stock update message for order {}: {}", order.getOrderId(), e.getMessage());
            // No lanzamos la excepción para no afectar el flujo principal
        }
    }

    // Clase interna para el mensaje de actualización de inventario
    private static class StockUpdateMessage {
        private final String orderId;
        private final List<ProductUpdate> products;

        public StockUpdateMessage(String orderId, List<ProductUpdate> products) {
            this.orderId = orderId;
            this.products = products;
        }

        public String getOrderId() {
            return orderId;
        }

        public List<ProductUpdate> getProducts() {
            return products;
        }
    }

    private static class ProductUpdate {
        private final String productId;
        private final int quantity;

        public ProductUpdate(String productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public String getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
} 