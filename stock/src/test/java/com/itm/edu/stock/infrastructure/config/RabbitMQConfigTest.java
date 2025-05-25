package com.itm.edu.stock.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = RabbitMQConfig.class)
class RabbitMQConfigTest {

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    @MockBean
    private ConnectionFactory connectionFactory;

    @Test
    void stockResponseQueue_ShouldCreateQueue() {
        // Act
        Queue queue = rabbitMQConfig.stockResponseQueue();

        // Assert
        assertNotNull(queue);
        assertEquals("stock.response.queue", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void stockUpdateQueue_ShouldCreateQueue() {
        // Act
        Queue queue = rabbitMQConfig.stockUpdateQueue();

        // Assert
        assertNotNull(queue);
        assertEquals("stock.update.queue", queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void stockResponseExchange_ShouldCreateExchange() {
        // Act
        DirectExchange exchange = rabbitMQConfig.stockResponseExchange();

        // Assert
        assertNotNull(exchange);
        assertEquals("stock.response.exchange", exchange.getName());
        assertTrue(exchange.isDurable());
    }

    @Test
    void stockUpdateExchange_ShouldCreateExchange() {
        // Act
        DirectExchange exchange = rabbitMQConfig.stockUpdateExchange();

        // Assert
        assertNotNull(exchange);
        assertEquals("stock.update.exchange", exchange.getName());
        assertTrue(exchange.isDurable());
    }

    @Test
    void stockResponseBinding_ShouldCreateBinding() {
        // Arrange
        Queue queue = rabbitMQConfig.stockResponseQueue();
        DirectExchange exchange = rabbitMQConfig.stockResponseExchange();

        // Act
        Binding binding = rabbitMQConfig.stockResponseBinding(queue, exchange);

        // Assert
        assertNotNull(binding);
        assertEquals("stock.response.key", binding.getRoutingKey());
        assertEquals(queue.getName(), binding.getDestination());
        assertEquals(exchange.getName(), binding.getExchange());
    }

    @Test
    void stockUpdateBinding_ShouldCreateBinding() {
        // Arrange
        Queue queue = rabbitMQConfig.stockUpdateQueue();
        DirectExchange exchange = rabbitMQConfig.stockUpdateExchange();

        // Act
        Binding binding = rabbitMQConfig.stockUpdateBinding(queue, exchange);

        // Assert
        assertNotNull(binding);
        assertEquals("stock.update.key", binding.getRoutingKey());
        assertEquals(queue.getName(), binding.getDestination());
        assertEquals(exchange.getName(), binding.getExchange());
    }

    @Test
    void jsonMessageConverter_ShouldCreateConverter() {
        // Act
        MessageConverter converter = rabbitMQConfig.jsonMessageConverter();

        // Assert
        assertNotNull(converter);
        assertTrue(converter instanceof Jackson2JsonMessageConverter);
    }

    @Test
    void rabbitTemplate_ShouldConfigureTemplate() {
        // Arrange
        MessageConverter converter = rabbitMQConfig.jsonMessageConverter();

        // Act
        RabbitTemplate template = rabbitMQConfig.rabbitTemplate(connectionFactory, converter);

        // Assert
        assertNotNull(template);
        assertEquals(converter, template.getMessageConverter());
        assertEquals("stock.response.exchange", template.getExchange());
        assertEquals("stock.response.key", template.getRoutingKey());
    }

    @Test
    void rabbitListenerContainerFactory_ShouldConfigureFactory() {
        // Arrange
        MessageConverter converter = rabbitMQConfig.jsonMessageConverter();

        // Act
        SimpleRabbitListenerContainerFactory factory = rabbitMQConfig.rabbitListenerContainerFactory(
            connectionFactory, converter);

        // Assert
        assertNotNull(factory);
    }
} 