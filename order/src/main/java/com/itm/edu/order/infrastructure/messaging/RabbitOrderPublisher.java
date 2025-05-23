package com.itm.edu.order.infrastructure.messaging;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.order.application.ports.outputs.OrderPublisherPort;
import com.itm.edu.order.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitOrderPublisher implements OrderPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(OrderMessageDTO orderMessage) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                orderMessage
            );
            log.info("✅ Mensaje de orden enviado a {}: {}", RabbitMQConfig.ORDER_EXCHANGE, orderMessage);
        } catch (Exception e) {
            log.error("❌ Error enviando mensaje: {}", e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

