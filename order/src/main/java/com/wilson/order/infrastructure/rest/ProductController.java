package com.wilson.order.infrastructure.rest;

import com.wilson.order.application.ports.inputs.CreateProductUseCase;
import com.wilson.order.application.ports.inputs.GetProductUseCase;
import com.wilson.order.domain.model.Product;
import com.wilson.order.infrastructure.rest.dto.CreateProductRequestDto;
import com.wilson.order.infrastructure.rest.dto.ProductDto;
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

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequestDto request) {
        Product product = createProductUseCase.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock()
        );
        return ResponseEntity.ok(mapToDto(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID id) {
        Product product = getProductUseCase.getProduct(id);
        return ResponseEntity.ok(mapToDto(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = getProductUseCase.getAllProducts();
        List<ProductDto> productDtos = products.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    private ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
} 