package com.wilson.order.application;

import com.wilson.order.domain.model.Order;
import com.wilson.order.domain.repository.OrderRepository;

public class OrderService {

    private OrderRepository orderRepository;

    public void taxes(){
        Order order = orderRepository.getOrder("12");
    }
}
