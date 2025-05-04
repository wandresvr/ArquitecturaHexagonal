package com.itm.edu.stock.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String ORDER_QUEUE       = "order.queue";
    public static final String ORDER_EXCHANGE    = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.key";

    @Bean Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }
    @Bean DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }
    @Bean Binding orderBinding(Queue q, DirectExchange ex) {
        return BindingBuilder.bind(q).to(ex).with(ORDER_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
