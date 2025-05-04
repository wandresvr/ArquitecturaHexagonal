package com.itm.edu.order.application.ports.outputs;

import com.itm.edu.common.dto.OrderMessageDTO;


public interface OrderPublisherPort {
    void publish(OrderMessageDTO message);
}
