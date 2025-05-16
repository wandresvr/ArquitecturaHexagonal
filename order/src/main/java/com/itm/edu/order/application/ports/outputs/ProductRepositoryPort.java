package com.itm.edu.order.application.ports.outputs;

import com.itm.edu.order.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    List<Product> findAll();
    void deleteById(UUID id);
} 