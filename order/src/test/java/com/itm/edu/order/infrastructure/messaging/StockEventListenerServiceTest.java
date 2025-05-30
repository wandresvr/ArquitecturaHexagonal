package com.itm.edu.order.infrastructure.messaging;

import com.itm.edu.order.application.dto.events.StockUpdateResponseEvent;
import com.itm.edu.order.application.dto.events.StockValidationStatus;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Product;
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
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePostProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.support.converter.MessageConverter;

import java.math.BigDecimal;
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

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StockEventListenerService stockEventListenerService;

    @Test
    void shouldHandleStockValidationSuccessEvent() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .orderId(orderId)
                .status(StockValidationStatus.RESERVED)
                .build();
        
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING_VALIDATION")
                .products(List.of())
                .build();

        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).findById(any(UUID.class));
        verify(orderRepository).save(any(Order.class));
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                any(List.class),
                any(MessagePostProcessor.class)
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

        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).findById(any(UUID.class));
        verify(orderRepository).save(any(Order.class));
        verify(rabbitTemplate, never()).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                any(List.class),
                any(MessagePostProcessor.class)
        );
    }

    @Test
    void shouldHandleOrderNotInPendingValidationState() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .orderId(orderId)
                .status(StockValidationStatus.RESERVED)
                .build();
        
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus("COMPLETED")
                .build();

        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(order));

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).findById(any(UUID.class));
        verify(orderRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                any(List.class),
                any(MessagePostProcessor.class)
        );
    }

    @Test
    void shouldHandleUnavailableStatus() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .orderId(orderId)
                .status(StockValidationStatus.UNAVAILABLE)
                .build();
        
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING_VALIDATION")
                .build();

        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).findById(any(UUID.class));
        verify(orderRepository).save(any(Order.class));
        verify(rabbitTemplate, never()).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                any(List.class),
                any(MessagePostProcessor.class)
        );
    }

    @Test
    void shouldHandleExceptionWhenSendingStockUpdateMessage() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .orderId(orderId)
                .status(StockValidationStatus.RESERVED)
                .build();
        
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING_VALIDATION")
                .products(List.of())
                .build();

        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        doThrow(new RuntimeException("Error al enviar mensaje"))
            .when(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                any(List.class),
                any(MessagePostProcessor.class)
            );

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                stockEventListenerService.handleStockValidationResponse(event));

        assertEquals("Error al procesar la respuesta de stock: Error al enviar actualización de stock: Error al enviar mensaje", 
                exception.getMessage());
        verify(orderRepository).findById(any(UUID.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldMapProductsCorrectlyWhenSendingStockUpdateMessage() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        StockUpdateResponseEvent event = StockUpdateResponseEvent.builder()
                .orderId(orderId)
                .status(StockValidationStatus.RESERVED)
                .build();
        
        Product product = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Product Description")
                .price(new BigDecimal("10.99"))
                .stock(100)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();

        List<OrderItem> orderItems = List.of(orderItem);
        
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus("PENDING_VALIDATION")
                .products(orderItems)
                .build();

        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        stockEventListenerService.handleStockValidationResponse(event);

        // Assert
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(argThat(updatedOrder -> 
            "STOCK_CONFIRMED".equals(updatedOrder.getOrderStatus())
        ));

        ArgumentCaptor<List<ProductOrderDTO>> productsCaptor = ArgumentCaptor.forClass(List.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                productsCaptor.capture(),
                any(MessagePostProcessor.class)
        );

        List<ProductOrderDTO> capturedProducts = productsCaptor.getValue();
        assertNotNull(capturedProducts);
        assertEquals(1, capturedProducts.size());
        
        ProductOrderDTO capturedProduct = capturedProducts.get(0);
        assertNotNull(capturedProduct);
        assertEquals(productId, capturedProduct.getProductId());
        assertEquals(2, capturedProduct.getQuantity());
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

        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                stockEventListenerService.handleStockValidationResponse(event));

        assertEquals("Orden no encontrada: " + orderId, exception.getMessage());
        verify(orderRepository).findById(any(UUID.class));
        verify(orderRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(
                eq(RabbitMQConfig.STOCK_UPDATE_EXCHANGE),
                eq(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY),
                any(List.class),
                any(MessagePostProcessor.class)
        );
    }
} 