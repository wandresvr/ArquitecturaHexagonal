package com.wilson.order.application.services;

import com.wilson.order.application.ports.inputs.GetProductUseCase;
import com.wilson.order.application.ports.outputs.ProductRepositoryPort;
import com.wilson.order.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetProductService implements GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public GetProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product getProduct(UUID id) {
        return productRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepositoryPort.findAll();
    }
} 