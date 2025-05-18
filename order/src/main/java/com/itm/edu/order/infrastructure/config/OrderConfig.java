package com.itm.edu.order.infrastructure.config;

import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.infrastructure.persistence.OrderRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {
    
    @Bean
    public OrderRepositoryPort orderRepositoryPort(OrderRepositoryAdapter orderRepositoryAdapter) {
        return orderRepositoryAdapter;
    }
} 