package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.DeleteProductUseCase;
import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.domain.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public DeleteProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public void deleteProduct(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        if (!productRepositoryPort.findById(id).isPresent()) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }

        productRepositoryPort.deleteById(id);
    }
} 