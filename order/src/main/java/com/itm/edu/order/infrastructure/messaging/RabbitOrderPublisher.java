package com.itm.edu.order.infrastructure.messaging;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.order.application.ports.outputs.OrderPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.itm.edu.order.infrastructure.config.RabbitMQConfig.ORDER_EXCHANGE;
import static com.itm.edu.order.infrastructure.config.RabbitMQConfig.ORDER_ROUTING_KEY;

@Component
@RequiredArgsConstructor
public class RabbitOrderPublisher implements OrderPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(OrderMessageDTO orderMessage) {
        try {
            rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, orderMessage);
            System.out.println("Message sent: " + orderMessage);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

