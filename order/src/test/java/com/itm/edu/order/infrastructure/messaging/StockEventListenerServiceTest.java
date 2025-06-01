package com.itm.edu.order.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.edu.order.application.dto.events.StockUpdateResponseEvent;
import com.itm.edu.order.application.dto.events.StockValidationStatus;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.common.dto.ProductOrderDTO;
import com.itm.edu.order.infrastructure.config.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.MessageProperties;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockEventListenerServiceTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<MessagePostProcessor> messagePostProcessorCaptor;

    @InjectMocks
    private StockEventListenerService stockEventListenerService;

    private UUID orderId;
    private Order order;
    private Product product;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        
        product = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("10.99"))
                .stock(100)
                .build();

        orderItem = OrderItem.create(product, 2);

        order = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING_VALIDATION")
                .products(Arrays.asList(orderItem))
                .build();
    }

    @Test
    void handleStockValidationResponse_WhenStockReserved_ShouldUpdateOrderStatusAndSendMessage() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, StockValidationStatus.RESERVED, null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).save(argThat(savedOrder -> 
            "STOCK_CONFIRMED".equals(savedOrder.getOrderStatus())
        ));
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );
    }

    @Test
    void handleStockValidationResponse_WhenStockCancelled_ShouldUpdateOrderStatus() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, StockValidationStatus.CANCELLED_NO_STOCK, null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).save(argThat(savedOrder -> 
            "CANCELLED_NO_STOCK".equals(savedOrder.getOrderStatus())
        ));
        verify(rabbitTemplate, never()).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );
    }

    @Test
    void handleStockValidationResponse_WhenStockUnavailable_ShouldUpdateOrderStatus() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, StockValidationStatus.UNAVAILABLE, null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).save(argThat(savedOrder -> 
            "UNAVAILABLE".equals(savedOrder.getOrderStatus())
        ));
        verify(rabbitTemplate, never()).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );
    }

    @Test
    void handleStockValidationResponse_WhenOrderNotFound_ShouldThrowException() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, StockValidationStatus.RESERVED, null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> 
            stockEventListenerService.handleStockValidationResponse(event)
        );
    }

    @Test
    void handleStockValidationResponse_WhenOrderNotInPendingValidation_ShouldNotUpdate() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, StockValidationStatus.RESERVED, null);
        Order orderWithDifferentStatus = Order.builder()
                .orderId(orderId)
                .orderStatus("COMPLETED")
                .products(Arrays.asList(orderItem))
                .build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderWithDifferentStatus));

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );
    }

    @Test
    void handleStockValidationResponse_WhenEventIsNull_ShouldNotProcess() {
        // Act
        stockEventListenerService.handleStockValidationResponse(null);

        // Assert
        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );
    }

    @Test
    void handleStockValidationResponse_WhenEventOrderIdIsNull_ShouldNotProcess() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(null, StockValidationStatus.RESERVED, null);

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );
    }

    @Test
    void handleStockValidationResponse_WhenEventStatusIsNull_ShouldNotProcess() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, null, null);

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );
    }

    @Test
    void handleStockValidationResponse_WhenExceptionOccurs_ShouldThrowBusinessException() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, StockValidationStatus.RESERVED, null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
            stockEventListenerService.handleStockValidationResponse(event)
        );
        assertTrue(exception.getMessage().contains("Error al procesar la respuesta de stock"));
    }

    @Test
    void sendStockUpdateMessage_WhenExceptionOccurs_ShouldThrowBusinessException() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, StockValidationStatus.RESERVED, null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        doThrow(new RuntimeException("Error de RabbitMQ")).when(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
            stockEventListenerService.handleStockValidationResponse(event)
        );
        assertTrue(exception.getMessage().contains("Error al enviar actualización de stock"));
    }

    @Test
    void handleStockValidationResponse_WhenMessagePostProcessorFails_ShouldHandleGracefully() {
        // Arrange
        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, StockValidationStatus.RESERVED, null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        doAnswer(invocation -> {
            MessagePostProcessor processor = invocation.getArgument(3);
            Message message = mock(Message.class);
            MessageProperties properties = mock(MessageProperties.class);
            when(message.getMessageProperties()).thenReturn(properties);
            return processor.postProcessMessage(message);
        }).when(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            any(List.class),
            any(MessagePostProcessor.class)
        );
    }

    @Test
    void handleStockValidationResponse_WhenOrderHasNoProducts_ShouldHandleGracefully() {
        // Arrange
        Order orderWithoutProducts = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING_VALIDATION")
                .products(new ArrayList<>())
                .build();

        StockUpdateResponseEvent event = new StockUpdateResponseEvent(orderId, StockValidationStatus.RESERVED, null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderWithoutProducts));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
            eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
            eq(new ArrayList<>()),
            any(MessagePostProcessor.class)
        );
    }
} 