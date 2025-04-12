package com.wilson.order.application.services;

import com.wilson.order.application.ports.inputs.CreateProductUseCase;
import com.wilson.order.application.ports.outputs.ProductRepositoryPort;
import com.wilson.order.domain.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public CreateProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product createProduct(String name, String description, BigDecimal price, Integer stock) {
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .build();
        
        return productRepositoryPort.save(product);
    }
} 