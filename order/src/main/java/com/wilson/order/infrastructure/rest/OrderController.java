package com.wilson.order.infrastructure.rest;

import com.wilson.order.application.ports.inputs.CreateOrderUseCase;
import com.wilson.order.domain.valueobjects.AddressShipping;
import com.wilson.order.domain.model.Order;
import com.wilson.order.infrastructure.rest.dto.AddressShippingDto;
import com.wilson.order.infrastructure.rest.dto.CreateOrderRequestDto;
import com.wilson.order.infrastructure.rest.dto.CreateOrderProductDto;
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
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequestDto request) {
        Map<UUID, BigDecimal> productQuantities = request.getProducts().stream()
                .collect(Collectors.toMap(
                        CreateOrderProductDto::getProductId,
                        CreateOrderProductDto::getQuantity
                ));

        AddressShipping addressShipping = AddressShipping.builder()
                .street(request.getShippingAddress().getStreet())
                .city(request.getShippingAddress().getCity())
                .state(request.getShippingAddress().getState())
                .zipCode(request.getShippingAddress().getZipCode())
                .country(request.getShippingAddress().getCountry())
                .build();

        Order order = createOrderUseCase.createOrder(
                request.getCustomerName(), 
                productQuantities,
                addressShipping
        );
        return ResponseEntity.ok(order);
    }
} 