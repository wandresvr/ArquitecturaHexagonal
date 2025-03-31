package com.wilson.order.application.service;

import com.wilson.order.application.ports.inputs.CreateOrderUseCase;
import com.wilson.order.domain.repository.ProductRepository;
import com.wilson.order.domain.repository.OrderRepository;
import com.wilson.order.domain.model.Product;
import com.wilson.order.domain.model.Order;
//import com.wilson.order.domain.model.OrderStatus;
import com.wilson.order.domain.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order createOrder(String customerName, Map<UUID, BigDecimal> productQuantities) {
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .orderDate(LocalDateTime.now())
                .build();

        productQuantities.forEach((productId, quantity) -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));
            order.addProduct(product, quantity);
        });

        return orderRepository.save(order);
    }
} 