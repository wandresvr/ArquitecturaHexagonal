package com.wilson.order.infrastructure.repository;

import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import com.wilson.order.domain.model.Order;
import com.wilson.order.domain.repository.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository {


    @Override
    public Order getOrder(String orderId) {
        return null;
    }
}
