package com.itm.edu.order.infrastructure.rest.dto;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "Los datos del cliente son requeridos")
    @Valid
    private CreateClientDto client;

    @NotNull(message = "La lista de productos es requerida")
    private Map<UUID, BigDecimal> productQuantities;

    @NotNull(message = "La dirección de envío es requerida")
    private AddressShipping addressShipping;
} 