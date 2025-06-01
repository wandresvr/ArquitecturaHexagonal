package com.itm.edu.order.infrastructure.rest.dto;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShippingAddressRequest {
    @NotNull(message = "La dirección de envío es requerida")
    private AddressShipping addressShipping;
} 