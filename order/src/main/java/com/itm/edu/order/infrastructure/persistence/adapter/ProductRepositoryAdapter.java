package com.itm.edu.order.infrastructure.persistence.adapter;

import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.infrastructure.persistence.repository.JpaProductRepository;
import com.itm.edu.order.infrastructure.persistence.mapper.ProductEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    
    private final JpaProductRepository jpaProductRepository;
    private final ProductEntityMapper productMapper;

    @Override
    public Product save(Product product) {
        var entity = productMapper.toEntity(product);
        var savedEntity = jpaProductRepository.save(entity);
        return productMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaProductRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll().stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaProductRepository.deleteById(id);
    }
} 