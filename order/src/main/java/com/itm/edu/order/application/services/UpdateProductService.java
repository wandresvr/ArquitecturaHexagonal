package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.UpdateProductUseCase;
import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.domain.exception.ProductNotFoundException;
import com.itm.edu.order.domain.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public UpdateProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product updateProduct(UUID id, String name, String description, BigDecimal price, Integer stock) {
        validateProductParameters(name, description, price, stock);

        Product existingProduct = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        existingProduct.setName(name);
        existingProduct.setDescription(description);
        existingProduct.setPrice(price);
        existingProduct.setStock(stock);

        return productRepositoryPort.save(existingProduct);
    }

    private void validateProductParameters(String name, String description, BigDecimal price, Integer stock) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }

        if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }

        if (price == null) {
            throw new IllegalArgumentException("Product price cannot be null");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }

        if (stock == null) {
            throw new IllegalArgumentException("Product stock cannot be null");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
    }
} 