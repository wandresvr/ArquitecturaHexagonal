package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.UpdateProductUseCase;
import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProductService implements UpdateProductUseCase {
    private final ProductRepositoryPort productRepository;

    @Override
    @Transactional
    public Product updateProduct(UUID id, Product product) {
        validateProductData(product);
        
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Producto no encontrado con ID: " + id));

        Product updatedProduct = Product.builder()
            .id(existingProduct.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .build();

        return productRepository.save(updatedProduct);
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