package com.itm.edu.stock.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // Exchange y cola para creación de órdenes
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.key";

    // Exchange y cola para respuestas de stock
    public static final String STOCK_RESPONSE_QUEUE = "stock.response.queue";
    public static final String STOCK_RESPONSE_EXCHANGE = "stock.response.exchange";
    public static final String STOCK_RESPONSE_ROUTING_KEY = "stock.response.key";

    // Exchange y cola para actualizaciones de inventario
    public static final String STOCK_UPDATE_QUEUE = "stock.update.queue";
    public static final String STOCK_UPDATE_EXCHANGE = "stock.update.exchange";
    public static final String STOCK_UPDATE_ROUTING_KEY = "stock.update.key";

    // Colas
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public Queue stockResponseQueue() {
        return new Queue(STOCK_RESPONSE_QUEUE, true);
    }

    @Bean
    public Queue stockUpdateQueue() {
        return new Queue(STOCK_UPDATE_QUEUE, true);
    }

    // Exchanges
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public DirectExchange stockResponseExchange() {
        return new DirectExchange(STOCK_RESPONSE_EXCHANGE);
    }

    @Bean
    public DirectExchange stockUpdateExchange() {
        return new DirectExchange(STOCK_UPDATE_EXCHANGE);
    }

    // Bindings
    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder
            .bind(orderQueue)
            .to(orderExchange)
            .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public Binding stockResponseBinding(Queue stockResponseQueue, DirectExchange stockResponseExchange) {
        return BindingBuilder
            .bind(stockResponseQueue)
            .to(stockResponseExchange)
            .with(STOCK_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding stockUpdateBinding(Queue stockUpdateQueue, DirectExchange stockUpdateExchange) {
        return BindingBuilder
            .bind(stockUpdateQueue)
            .to(stockUpdateExchange)
            .with(STOCK_UPDATE_ROUTING_KEY);
    }

    // Configuración de mensajes JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }
}
