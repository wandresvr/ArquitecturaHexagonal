package com.itm.edu.order.infrastructure.rest;

import com.itm.edu.order.application.ports.inputs.CreateOrderUseCase;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.infrastructure.rest.dto.AddressShippingDto;
import com.itm.edu.order.infrastructure.rest.dto.CreateClientDto;
import com.itm.edu.order.infrastructure.rest.dto.CreateOrderProductDto;
import com.itm.edu.order.infrastructure.rest.dto.CreateOrderRequestDto;
import com.itm.edu.order.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequestDto request) {
        validateOrderRequest(request);

        // Crear el cliente
        Client client = Client.builder()
                .name(request.getClient().getName())
                .email(request.getClient().getEmail())
                .phone(request.getClient().getPhone())
                .build();

        // Convertir productos a mapa de cantidades
        Map<UUID, BigDecimal> productQuantities = request.getProducts().stream()
                .collect(Collectors.toMap(
                        CreateOrderProductDto::getProductId,
                        CreateOrderProductDto::getQuantity
                ));

        // Crear la dirección de envío
        AddressShipping addressShipping = AddressShipping.builder()
                .street(request.getShippingAddress().getStreet())
                .city(request.getShippingAddress().getCity())
                .state(request.getShippingAddress().getState())
                .zipCode(request.getShippingAddress().getZipCode())
                .country(request.getShippingAddress().getCountry())
                .build();

        try {
            // Crear la orden
            Order order = createOrderUseCase.createOrder(
                    client,
                    productQuantities,
                    addressShipping
            );
            return ResponseEntity.ok(order);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void validateOrderRequest(CreateOrderRequestDto request) {
        if (request.getClient() == null) {
            throw new IllegalArgumentException("Client information is required");
        }

        if (request.getProducts() == null || request.getProducts().isEmpty()) {
            throw new IllegalArgumentException("At least one product is required");
        }

        for (CreateOrderProductDto product : request.getProducts()) {
            if (product.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Product quantity must be greater than 0");
            }
        }

        if (request.getShippingAddress() == null) {
            throw new IllegalArgumentException("Shipping address is required");
        }
    }
} 