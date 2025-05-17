package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.*;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.infrastructure.rest.dto.*;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Órdenes", description = "API para la gestión de órdenes")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final UpdateShippingAddressUseCase updateShippingAddressUseCase;

    @Operation(summary = "Crear una nueva orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden creada exitosamente",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "Error de validación",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequestDto request) {
        log.info("Recibida solicitud de creación de orden: {}", request);
        
        try {
            validateOrderRequest(request);

            // Crear el cliente
            Client client = Client.builder()
                    .name(request.getClient().getName())
                    .email(request.getClient().getEmail())
                    .phone(request.getClient().getPhone())
                    .build();
            log.debug("Cliente creado: {}", client);

            // Convertir productos a mapa de cantidades
            Map<UUID, BigDecimal> productQuantities = request.getProducts().stream()
                    .collect(Collectors.toMap(
                            CreateOrderProductDto::getProductId,
                            CreateOrderProductDto::getQuantity
                    ));
            log.debug("Productos convertidos: {}", productQuantities);

            // Crear la dirección de envío
            AddressShipping addressShipping = AddressShipping.builder()
                    .street(request.getShippingAddress().getStreet())
                    .city(request.getShippingAddress().getCity())
                    .state(request.getShippingAddress().getState())
                    .zipCode(request.getShippingAddress().getZipCode())
                    .country(request.getShippingAddress().getCountry())
                    .build();
            log.debug("Dirección de envío creada: {}", addressShipping);

            // Crear la orden
            Order order = createOrderUseCase.createOrder(
                    client,
                    productQuantities,
                    addressShipping
            );
            log.info("Orden creada exitosamente: {}", order);
            
            return ResponseEntity.ok(OrderResponseDto.fromDomain(order));
            
        } catch (BusinessException e) {
            log.error("Error de negocio al crear la orden: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiError.of(HttpStatus.BAD_REQUEST, e.getMessage(), "/api/orders"));
        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), "/api/orders"));
        } catch (Exception e) {
            log.error("Error inesperado al crear la orden", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, 
                        "Error interno del servidor al crear la orden", "/api/orders"));
        }
    }

    @Operation(summary = "Obtener una orden por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden encontrada",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable UUID id) {
        return getOrderUseCase.getOrder(id)
                .map(order -> ResponseEntity.ok(OrderResponseDto.fromDomain(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todas las órdenes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(getOrderUseCase.getAllOrders().stream()
                .map(OrderResponseDto::fromDomain)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Actualizar una orden existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "Error de validación",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable UUID id,
            @RequestBody UpdateOrderRequestDto request) {
        try {
            Order orderDetails = Order.builder()
                    .orderStatus(request.getOrderStatus())
                    .build();

            Order updatedOrder = updateOrderUseCase.updateOrder(id, orderDetails);
            return ResponseEntity.ok(OrderResponseDto.fromDomain(updatedOrder));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar una orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden eliminada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable UUID id) {
        try {
            deleteOrderUseCase.deleteOrder(id);
            return ResponseEntity.ok().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar la dirección de envío de una orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dirección de envío actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "Error de validación",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PatchMapping("/{id}/shipping-address")
    public ResponseEntity<?> updateShippingAddress(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable UUID id,
            @RequestBody AddressShippingDto addressDto) {
        try {
            AddressShipping address = AddressShipping.builder()
                    .street(addressDto.getStreet())
                    .city(addressDto.getCity())
                    .state(addressDto.getState())
                    .zipCode(addressDto.getZipCode())
                    .country(addressDto.getCountry())
                    .build();

            Order updatedOrder = updateShippingAddressUseCase.updateShippingAddress(id, address);
            return ResponseEntity.ok(OrderResponseDto.fromDomain(updatedOrder));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void validateOrderRequest(CreateOrderRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        if (request.getClient() == null) {
            throw new IllegalArgumentException("La información del cliente es requerida");
        }

        if (request.getClient().getName() == null || request.getClient().getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es requerido");
        }

        if (request.getClient().getEmail() == null || request.getClient().getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del cliente es requerido");
        }

        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos un producto");
        }

        for (CreateOrderProductDto product : request.getProducts()) {
            if (product.getProductId() == null) {
                throw new IllegalArgumentException("El ID del producto es requerido");
            }
            if (product.getQuantity() == null || product.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("La cantidad del producto debe ser mayor a 0");
            }
        }

        if (request.getShippingAddress() == null) {
            throw new IllegalArgumentException("La dirección de envío es requerida");
        }

        if (request.getShippingAddress().getStreet() == null || request.getShippingAddress().getStreet().trim().isEmpty()) {
            throw new IllegalArgumentException("La calle de la dirección de envío es requerida");
        }
    }
} 