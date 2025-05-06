package com.itm.edu.stock.infrastructure.messaging;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.stock.application.ports.input.ProcessOrderUseCase;
import com.itm.edu.stock.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;

import static com.itm.edu.stock.infrastructure.config.RabbitMQConfig.ORDER_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitOrderConsumer {

    private final ProcessOrderUseCase processOrderUseCase;

    @RabbitListener(
      queues = ORDER_QUEUE,
      containerFactory = "rabbitListenerContainerFactory"
    )
    public void onOrderMessage(OrderMessageDTO msg) {
        try {
            processOrderUseCase.processOrder(msg);
            log.info("Orden procesada correctamente: {}", msg.getOrderId());
        } catch (BusinessException e) {
            log.error("Error de negocio procesando la orden {}: {}", msg.getOrderId(), e.getMessage());
            // Lanzar excepción que indica que no debe reintentar
            throw new AmqpRejectAndDontRequeueException("Error de negocio: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error inesperado procesando la orden {}: {}", msg.getOrderId(), e.getMessage(), e);
            throw new AmqpRejectAndDontRequeueException("Error inesperado: " + e.getMessage(), e);
        }
    }
}
