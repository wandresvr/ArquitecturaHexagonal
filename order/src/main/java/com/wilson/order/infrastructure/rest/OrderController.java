package com.wilson.order.infrastructure.rest;

import com.wilson.order.application.ports.inputs.CreateOrderUseCase;
import com.wilson.order.domain.model.Order;
import com.wilson.order.infrastructure.rest.dto.CreateOrderRequestDto;
import com.wilson.order.infrastructure.rest.dto.CreateOrderProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Map<UUID, BigDecimal> ingredientQuantities = request.getIngredients().stream()
                .collect(Collectors.toMap(
                        CreateOrderProductDto::getIngredientId,
                        CreateOrderProductDto::getQuantity
                ));

        Order order = createOrderUseCase.createOrder(request.getCustomerName(), ingredientQuantities);
        return ResponseEntity.ok(order);
    }
} 