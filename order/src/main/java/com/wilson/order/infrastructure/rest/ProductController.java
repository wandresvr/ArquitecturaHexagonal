package com.wilson.order.infrastructure.rest;

import com.wilson.order.application.ports.inputs.CreateProductUseCase;
import com.wilson.order.application.ports.inputs.GetProductUseCase;
import com.wilson.order.domain.model.Product;
import com.wilson.order.infrastructure.rest.dto.ProductDto;
import com.wilson.order.infrastructure.rest.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto request) {
        Product product = createProductUseCase.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock()
        );
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID id) {
        return getProductUseCase.getProduct(id)
                .map(product -> ResponseEntity.ok(productMapper.toDto(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = getProductUseCase.getAllProducts();
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }
} 