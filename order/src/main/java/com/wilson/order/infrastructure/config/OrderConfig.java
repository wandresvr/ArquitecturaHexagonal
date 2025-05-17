package com.wilson.order.infrastructure.config;

import com.wilson.order.application.ports.outputs.OrderRepositoryPort;
import com.wilson.order.infrastructure.persistence.OrderRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {
    
    @Bean
    public OrderRepositoryPort orderRepositoryPort(OrderRepositoryAdapter orderRepositoryAdapter) {
        return orderRepositoryAdapter;
    }
} 