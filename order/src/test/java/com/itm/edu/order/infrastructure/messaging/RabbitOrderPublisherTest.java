package com.itm.edu.order.infrastructure.messaging;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.order.infrastructure.config.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitOrderPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitOrderPublisher rabbitOrderPublisher;

    private OrderMessageDTO orderMessage;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        orderMessage = OrderMessageDTO.builder()
            .orderId(orderId)
            .build();
    }

    @Test
    void publish_ShouldSendMessageSuccessfully() {
        // Act
        rabbitOrderPublisher.publish(orderMessage);

        // Assert
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.ORDER_EXCHANGE),
            eq(RabbitMQConfig.ORDER_ROUTING_KEY),
            eq(orderMessage)
        );
    }

    @Test
    void publish_WhenExceptionOccurs_ShouldPropagateException() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Error de conexión");
        doThrow(expectedException)
            .when(rabbitTemplate)
            .convertAndSend(
                eq(RabbitMQConfig.ORDER_EXCHANGE),
                eq(RabbitMQConfig.ORDER_ROUTING_KEY),
                eq(orderMessage)
            );

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            rabbitOrderPublisher.publish(orderMessage)
        );
        assertEquals("Error de conexión", exception.getMessage());
    }

    @Test
    void publish_WhenMessageIsNull_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            rabbitOrderPublisher.publish(null)
        );
        assertEquals("El mensaje de orden no puede ser nulo", exception.getMessage());
    }
} 