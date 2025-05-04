package com.itm.edu.stock.infrastructure.messaging;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.stock.application.ports.input.ProcessOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitOrderConsumer {

    private final ProcessOrderUseCase processOrderUseCase;

    @RabbitListener(queues = "order.queue")
    public void onOrderMessage(OrderMessageDTO msg) {
        processOrderUseCase.processOrder(msg);
    }
}
