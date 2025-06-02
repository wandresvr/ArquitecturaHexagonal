package com.itm.edu.order.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
    private CreateClientDto client;
    private List<CreateOrderProductDto> products;
    private AddressShippingDto shippingAddress;
} 