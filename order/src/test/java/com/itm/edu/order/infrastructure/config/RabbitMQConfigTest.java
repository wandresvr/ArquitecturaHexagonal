package com.itm.edu.order.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
@ContextConfiguration(classes = {RabbitMQConfig.class})
@TestPropertySource(properties = {
    "spring.rabbitmq.host=localhost",
    "spring.rabbitmq.port=5672",
    "spring.rabbitmq.username=guest",
    "spring.rabbitmq.password=guest",
    "spring.rabbitmq.listener.simple.retry.enabled=false",
    "spring.rabbitmq.listener.simple.auto-startup=false"
})
class RabbitMQConfigTest {

    @MockBean
    private ConnectionFactory connectionFactory;

    @Autowired
    private Queue orderQueue;

    @Autowired
    private Queue stockResponseQueue;

    @Autowired
    private Queue stockUpdateQueue;

    @Autowired
    private DirectExchange orderExchange;

    @Autowired
    private DirectExchange stockResponseExchange;

    @Autowired
    private DirectExchange stockUpdateExchange;

    @Autowired
    private Binding orderBinding;

    @Autowired
    private Binding stockResponseBinding;

    @Autowired
    private Binding stockUpdateBinding;

    @Autowired
    private MessageConverter jsonMessageConverter;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

    @Test
    void shouldCreateAllBeans() {
        assertNotNull(orderQueue);
        assertNotNull(stockResponseQueue);
        assertNotNull(stockUpdateQueue);
        assertNotNull(orderExchange);
        assertNotNull(stockResponseExchange);
        assertNotNull(stockUpdateExchange);
        assertNotNull(orderBinding);
        assertNotNull(stockResponseBinding);
        assertNotNull(stockUpdateBinding);
        assertNotNull(jsonMessageConverter);
        assertNotNull(rabbitTemplate);
        assertNotNull(rabbitListenerContainerFactory);
    }

    @Test
    void shouldConfigureQueuesCorrectly() {
        assertEquals(RabbitMQConfig.ORDER_QUEUE, orderQueue.getName());
        assertEquals(RabbitMQConfig.STOCK_RESPONSE_QUEUE, stockResponseQueue.getName());
        assertEquals(RabbitMQConfig.STOCK_UPDATE_QUEUE, stockUpdateQueue.getName());
        assertTrue(orderQueue.isDurable());
        assertTrue(stockResponseQueue.isDurable());
        assertTrue(stockUpdateQueue.isDurable());
    }

    @Test
    void shouldConfigureExchangesCorrectly() {
        assertEquals(RabbitMQConfig.ORDER_EXCHANGE, orderExchange.getName());
        assertEquals(RabbitMQConfig.STOCK_RESPONSE_EXCHANGE, stockResponseExchange.getName());
        assertEquals(RabbitMQConfig.STOCK_UPDATE_EXCHANGE, stockUpdateExchange.getName());
    }

    @Test
    void shouldConfigureBindingsCorrectly() {
        assertEquals(RabbitMQConfig.ORDER_ROUTING_KEY, orderBinding.getRoutingKey());
        assertEquals(RabbitMQConfig.STOCK_RESPONSE_ROUTING_KEY, stockResponseBinding.getRoutingKey());
        assertEquals(RabbitMQConfig.STOCK_UPDATE_ROUTING_KEY, stockUpdateBinding.getRoutingKey());
    }

    @Test
    void shouldConfigureRabbitTemplate() {
        assertNotNull(rabbitTemplate.getMessageConverter());
        assertTrue(rabbitTemplate.getMessageConverter() instanceof Jackson2JsonMessageConverter);
    }

    @Test
    void shouldConfigureRabbitListenerContainerFactory() {
        assertNotNull(rabbitListenerContainerFactory);
        assertTrue(jsonMessageConverter instanceof Jackson2JsonMessageConverter);
    }
} 