package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.CreateProductUseCase;
import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreateProductService implements CreateProductUseCase {
    private final ProductRepositoryPort productRepository;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        validateProductData(product);
        return productRepository.save(product);
    }

    private void validateProductData(Product product) {
        if (product == null) {
            throw new BusinessException("El producto no puede ser nulo");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new BusinessException("El nombre del producto no puede estar vacío");
        }
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            throw new BusinessException("La descripción del producto no puede estar vacía");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("El precio no puede ser negativo");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }
    }
} 