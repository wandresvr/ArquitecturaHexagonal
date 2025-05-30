package com.itm.edu.order.infrastructure.messaging;

import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.application.dto.events.StockUpdateResponseEvent;
import com.itm.edu.order.application.dto.events.StockValidationStatus;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.infrastructure.config.RabbitMQConfig;
import com.itm.edu.common.dto.ProductOrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListenerService {
    private final OrderRepositoryPort orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbitmq.queues.stock-response}")
    @Transactional
    public void handleStockValidationResponse(StockUpdateResponseEvent event) {
        log.info("Recibida respuesta de validación de stock para orden: {}", event);

        if (event == null || event.getOrderId() == null || event.getStatus() == null) {
            log.error("Evento de stock inválido recibido");
            return;
        }

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new BusinessException("Orden no encontrada: " + event.getOrderId()));

        if (!"PENDING_VALIDATION".equals(order.getOrderStatus())) {
            log.warn("La orden {} no está en estado PENDING_VALIDATION (estado actual: {})", 
                    order.getOrderId(), order.getOrderStatus());
            return;
        }

        try {
            String newStatus = determineOrderStatus(event.getStatus());
            Order updatedOrder = order.withUpdatedStatus(newStatus);
            orderRepository.save(updatedOrder);
            log.info("Estado de la orden {} actualizado a: {}", updatedOrder.getOrderId(), updatedOrder.getOrderStatus());
            
            if (event.getStatus() == StockValidationStatus.RESERVED) {
                sendStockUpdateMessage(updatedOrder);
            }
        } catch (Exception e) {
            log.error("Error al actualizar el estado de la orden {}: {}", order.getOrderId(), e.getMessage());
            throw new BusinessException("Error al procesar la respuesta de stock: " + e.getMessage());
        }
    }

    private String determineOrderStatus(StockValidationStatus status) {
        return switch (status) {
            case RESERVED -> "STOCK_CONFIRMED";
            case CANCELLED_NO_STOCK -> "CANCELLED_NO_STOCK";
            case UNAVAILABLE -> "UNAVAILABLE";
        };
    }

    private void sendStockUpdateMessage(Order order) {
        try {
            List<ProductOrderDTO> products = order.getProducts().stream()
                .map(item -> ProductOrderDTO.builder()
                    .productId(item.getProduct().getId())
                    .quantity(item.getQuantity())
                    .build())
                .collect(Collectors.toList());

            rabbitTemplate.convertAndSend(
                RabbitMQConfig.STOCK_UPDATE_EXCHANGE,
                RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY,
                products,
                message -> {
                    message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                    return message;
                }
            );
            log.info("Mensaje de actualización de stock enviado para orden: {}", order.getOrderId());
        } catch (Exception e) {
            log.error("Error enviando mensaje de actualización de stock para orden {}: {}", 
                order.getOrderId(), e.getMessage());
            throw new BusinessException("Error al enviar actualización de stock: " + e.getMessage());
        }
    }
} 