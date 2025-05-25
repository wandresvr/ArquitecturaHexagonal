package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.*;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.exception.ApiError;
import com.itm.edu.order.domain.exception.BusinessException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
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
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto request) {
        try {
            Product product = productMapper.toDomain(request);
            Product createdProduct = createProductUseCase.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDto(createdProduct));
        } catch (BusinessException e) {
            log.error("Error de negocio al crear el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), "/api/v1/products"));
        } catch (Exception e) {
            log.error("Error inesperado al crear el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", "/api/v1/products"));
        }
    }

    @Operation(summary = "Obtener un producto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "ID inválido",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable UUID id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest()
                    .body(ApiError.of(HttpStatus.BAD_REQUEST, "El ID del producto no puede ser nulo", "/api/v1/products"));
            }

            return getProductUseCase.getProduct(id)
                .map(product -> ResponseEntity.ok(productMapper.toDto(product)))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error al obtener el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", "/api/v1/products"));
        }
    }

    @Operation(summary = "Obtener todos los productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = getProductUseCase.getAllProducts();
            List<ProductDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
            return ResponseEntity.ok(productDtos);
        } catch (Exception e) {
            log.error("Error al obtener los productos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", "/api/v1/products"));
        }
    }

    @Operation(summary = "Actualizar un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "400", description = "ID inválido o solicitud inválida",
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
    public ResponseEntity<?> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDto request) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest()
                    .body(ApiError.of(HttpStatus.BAD_REQUEST, "El ID del producto no puede ser nulo", "/api/v1/products"));
            }

            Product product = productMapper.toDomain(request);
            Product updatedProduct = updateProductUseCase.updateProduct(id, product);
            return ResponseEntity.ok(productMapper.toDto(updatedProduct));
        } catch (BusinessException e) {
            log.error("Error de negocio al actualizar el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), "/api/v1/products"));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", "/api/v1/products"));
        }
    }

    @Operation(summary = "Eliminar un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inválido",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest()
                    .body(ApiError.of(HttpStatus.BAD_REQUEST, "El ID del producto no puede ser nulo", "/api/v1/products"));
            }

            deleteProductUseCase.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (BusinessException e) {
            log.error("Error de negocio al eliminar el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), "/api/v1/products"));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", "/api/v1/products"));
        }
    }
} 