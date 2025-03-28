package com.wilson.order.domain.repository;

import com.wilson.order.domain.model.Order;

public interface OrderRepository {

    Order getOrder(String orderId);
}
