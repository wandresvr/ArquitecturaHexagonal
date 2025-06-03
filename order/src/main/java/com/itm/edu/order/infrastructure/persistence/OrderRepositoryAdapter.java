package com.itm.edu.order.infrastructure.persistence;

import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.infrastructure.persistence.entities.OrderEntity;
import com.itm.edu.order.infrastructure.persistence.mapper.OrderMapper;
import com.itm.edu.order.infrastructure.persistence.repository.JpaOrderRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order save(Order order) {
        OrderEntity entity = orderMapper.toEntity(order);
        OrderEntity savedEntity = jpaOrderRepository.save(entity);
        return orderMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return jpaOrderRepository.findById(orderId)
                .map(orderMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return jpaOrderRepository.findAll().stream()
                .map(orderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID orderId) {
        jpaOrderRepository.deleteById(orderId);
    }

    @Override
    public Order update(Order order) {
        OrderEntity entity = orderMapper.toEntity(order);
        OrderEntity updatedEntity = jpaOrderRepository.save(entity);
        return orderMapper.toDomain(updatedEntity);
    }

    @Override
    public void flush() {
        jpaOrderRepository.flush();
    }
} 