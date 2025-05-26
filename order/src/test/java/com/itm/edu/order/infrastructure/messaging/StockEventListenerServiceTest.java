package com.itm.edu.order.infrastructure.messaging;

import com.itm.edu.order.application.dto.events.StockUpdateResponseEvent;
import com.itm.edu.order.application.dto.events.StockValidationStatus;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.infrastructure.config.RabbitMQConfig;
import com.itm.edu.common.dto.ProductOrderDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockEventListenerServiceTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private StockEventListenerService stockEventListenerService;

    @Test
    void shouldHandleStockValidationSuccessEvent() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .orderId(orderId)
                .status(StockValidationStatus.RESERVED)
                .build();
        
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING_VALIDATION")
                .build();

        Order updatedOrder = Order.builder()
                .orderId(orderId)
                .orderStatus("STOCK_CONFIRMED")
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                any(List.class)
        );
    }

    @Test
    void shouldHandleStockValidationRejectedEvent() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .orderId(orderId)
                .status(StockValidationStatus.CANCELLED_NO_STOCK)
                .reason("No hay stock disponible")
                .build();
        
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING_VALIDATION")
                .build();

        Order updatedOrder = Order.builder()
                .orderId(orderId)
                .orderStatus("CANCELLED_NO_STOCK")
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
        verify(rabbitTemplate, never()).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                any(List.class)
        );
    }

    @Test
    void shouldHandleNullEvent() {
        // Act
        stockEventListenerService.handleStockValidationResponse(null);

        // Assert
        verifyNoInteractions(orderRepository, rabbitTemplate);
    }

    @Test
    void shouldHandleEventWithNullOrderId() {
        // Arrange
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .status(StockValidationStatus.RESERVED)
                .build();

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verifyNoInteractions(orderRepository, rabbitTemplate);
    }

    @Test
    void shouldHandleEventWithNullStatus() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .orderId(orderId)
                .build();

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verifyNoInteractions(orderRepository, rabbitTemplate);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .orderId(orderId)
                .status(StockValidationStatus.RESERVED)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                stockEventListenerService.handleStockValidationResponse(event));

        assertEquals("Orden no encontrada: " + orderId, exception.getMessage());
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                any(List.class)
        );
    }
} 