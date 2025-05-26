package com.itm.edu.stock.infrastructure.messaging;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.common.dto.events.StockValidationStatus;
import com.itm.edu.common.dto.events.StockUpdateResponseEvent;
import com.itm.edu.stock.application.ports.input.ProcessOrderUseCase;
import com.itm.edu.stock.domain.exception.BusinessException;
import com.itm.edu.stock.infrastructure.config.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitOrderConsumerTest {

    @Mock
    private ProcessOrderUseCase processOrderUseCase;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitOrderConsumer rabbitOrderConsumer;

    @Captor
    private ArgumentCaptor<StockUpdateResponseEvent> responseCaptor;

    private OrderMessageDTO testMessage;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        testMessage = new OrderMessageDTO();
        testMessage.setOrderId(orderId);
    }

    @Test
    void onOrderMessage_Success() {
        // Act
        rabbitOrderConsumer.onOrderMessage(testMessage);

        // Assert
        verify(processOrderUseCase).processOrder(testMessage);
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.STOCK_RESPONSE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_RESPONSE_ROUTING_KEY),
            responseCaptor.capture()
        );

        StockUpdateResponseEvent response = responseCaptor.getValue();
        assertEquals(orderId, response.getOrderId());
        assertEquals(StockValidationStatus.RESERVED, response.getStatus());
    }

    @Test
    void onOrderMessage_BusinessException() {
        // Arrange
        doThrow(new BusinessException("No hay stock suficiente"))
            .when(processOrderUseCase).processOrder(any(OrderMessageDTO.class));

        // Act
        rabbitOrderConsumer.onOrderMessage(testMessage);

        // Assert
        verify(processOrderUseCase).processOrder(testMessage);
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.STOCK_RESPONSE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_RESPONSE_ROUTING_KEY),
            responseCaptor.capture()
        );

        StockUpdateResponseEvent response = responseCaptor.getValue();
        assertEquals(orderId, response.getOrderId());
        assertEquals(StockValidationStatus.CANCELLED_NO_STOCK, response.getStatus());
    }

    @Test
    void onOrderMessage_UnexpectedException() {
        // Arrange
        RuntimeException unexpectedException = new RuntimeException("Error inesperado");
        doThrow(unexpectedException)
            .when(processOrderUseCase).processOrder(any(OrderMessageDTO.class));

        // Act & Assert
        AmqpRejectAndDontRequeueException exception = assertThrows(
            AmqpRejectAndDontRequeueException.class,
            () -> rabbitOrderConsumer.onOrderMessage(testMessage)
        );

        verify(processOrderUseCase).processOrder(testMessage);
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.STOCK_RESPONSE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_RESPONSE_ROUTING_KEY),
            responseCaptor.capture()
        );

        StockUpdateResponseEvent response = responseCaptor.getValue();
        assertEquals(orderId, response.getOrderId());
        assertEquals(StockValidationStatus.CANCELLED_NO_STOCK, response.getStatus());
        assertEquals("Error inesperado: Error inesperado", exception.getMessage());
        assertEquals(unexpectedException, exception.getCause());
    }

    @Test
    void onOrderMessage_NullMessage() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> rabbitOrderConsumer.onOrderMessage(null));

        verify(processOrderUseCase, never()).processOrder(any());
        verifyNoInteractions(rabbitTemplate);
    }
} 