package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.*;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.exception.ApiError;
import com.itm.edu.order.infrastructure.rest.dto.ProductDto;
import com.itm.edu.order.infrastructure.rest.mapper.ProductDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "API para la gestión de productos")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final ProductDtoMapper productMapper;

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un producto con ese nombre",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "Error de validación",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto request) {
        Product product = productMapper.toDomain(request);
        Product createdProduct = createProductUseCase.createProduct(product);
        return ResponseEntity.ok(productMapper.toDto(createdProduct));
    }

    @Operation(summary = "Obtener un producto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID id) {
        return getProductUseCase.getProduct(id)
            .map(product -> ResponseEntity.ok(productMapper.toDto(product)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todos los productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = getProductUseCase.getAllProducts();
        List<ProductDto> productDtos = products.stream()
            .map(productMapper::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @Operation(summary = "Actualizar un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un producto con ese nombre",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "Error de validación",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDto request) {
        Product product = productMapper.toDomain(request);
        Product updatedProduct = updateProductUseCase.updateProduct(id, product);
        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }

    @Operation(summary = "Eliminar un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        deleteProductUseCase.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
} 