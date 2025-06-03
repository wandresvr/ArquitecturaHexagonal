package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.*;
import com.itm.edu.order.domain.model.*;
import com.itm.edu.order.domain.valueobjects.*;
import com.itm.edu.order.infrastructure.rest.dto.OrderDto;
import com.itm.edu.order.infrastructure.rest.dto.AddressShippingDto;
import com.itm.edu.order.infrastructure.rest.dto.CreateOrderRequest;
import com.itm.edu.order.infrastructure.rest.dto.UpdateShippingAddressRequest;
import com.itm.edu.order.infrastructure.rest.mapper.OrderDtoMapper;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.domain.exception.ApiError;
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
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Órdenes", description = "API para la gestión de órdenes")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final UpdateShippingAddressUseCase updateShippingAddressUseCase;
    private final OrderDtoMapper orderDtoMapper;

    @Operation(summary = "Crear una nueva orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden creada exitosamente",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "Error de validación",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            log.info("Recibida solicitud para crear orden: {}", request);

            // Validar cliente
            if (request.getClient() == null) {
                log.warn("Intento de crear orden sin cliente");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, "El cliente no puede ser nulo", "/api/v1/orders"));
            }

            // Validar dirección de envío
            if (request.getAddressShipping() == null) {
                log.warn("Intento de crear orden sin dirección de envío");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, "La dirección de envío no puede ser nula", "/api/v1/orders"));
            }

            // Validar productos y cantidades
            Map<UUID, BigDecimal> productQuantities = request.getProductQuantities();
            if (productQuantities == null || productQuantities.isEmpty()) {
                log.warn("Intento de crear orden sin productos");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, "Debe especificar al menos un producto", "/api/v1/orders"));
            }

            // Validar cantidades negativas o cero
            for (Map.Entry<UUID, BigDecimal> entry : productQuantities.entrySet()) {
                if (entry.getValue() == null || entry.getValue().compareTo(BigDecimal.ZERO) <= 0) {
                    log.warn("Intento de crear orden con cantidad inválida para producto {}: {}", entry.getKey(), entry.getValue());
                    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, "La cantidad debe ser mayor a cero", "/api/v1/orders"));
                }
            }

            log.info("Creando orden con {} productos", productQuantities.size());
            Order order = createOrderUseCase.createOrder(
                request.getClient(),
                request.getProductQuantities(),
                request.getAddressShipping()
            );
            
            log.info("Orden creada exitosamente con ID: {}", order.getOrderId());
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDtoMapper.toDto(order));
        } catch (BusinessException e) {
            log.error("Error de negocio al crear la orden: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiError.of(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), "/api/v1/orders"));
        } catch (Exception e) {
            log.error("Error inesperado al crear la orden: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor: " + e.getMessage(), "/api/v1/orders"));
        }
    }

    @Operation(summary = "Obtener una orden por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden encontrada",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable UUID id) {
        return getOrderUseCase.getOrder(id)
                .map(order -> ResponseEntity.ok(orderDtoMapper.toDto(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener todas las órdenes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<Order> orders = getOrderUseCase.getAllOrders();
        List<OrderDto> orderDtos = orders.stream()
                .map(orderDtoMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Actualizar una orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
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
    public ResponseEntity<OrderDto> updateOrder(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable UUID id,
            @RequestBody OrderDto request) {
        Order orderDetails = Order.builder()
                .orderStatus(request.getOrderStatus())
                .build();

        Order updatedOrder = updateOrderUseCase.updateOrder(id, orderDetails);
        return ResponseEntity.ok(orderDtoMapper.toDto(updatedOrder));
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
    public ResponseEntity<?> deleteOrder(@PathVariable UUID id) {
        try {
            deleteOrderUseCase.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (BusinessException e) {
            return ResponseEntity.badRequest()
                .body(ApiError.of(HttpStatus.BAD_REQUEST, e.getMessage(), "/api/orders"));
        }
    }

    @Operation(summary = "Actualizar la dirección de envío de una orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dirección de envío actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "422", description = "Error de validación",
            content = @Content(schema = @Schema(implementation = ApiError.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{orderId}/shipping-address")
    public ResponseEntity<OrderDto> updateShippingAddress(
            @Parameter(description = "ID de la orden", required = true)
            @PathVariable UUID orderId,
            @Valid @RequestBody UpdateShippingAddressRequest request) {
        try {
            Order updatedOrder = updateShippingAddressUseCase.updateShippingAddress(
                orderId,
                request.getAddressShipping()
            );
            return ResponseEntity.ok(orderDtoMapper.toDto(updatedOrder));
        } catch (BusinessException e) {
            log.error("Error de negocio al actualizar la dirección: {}", e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }
} 