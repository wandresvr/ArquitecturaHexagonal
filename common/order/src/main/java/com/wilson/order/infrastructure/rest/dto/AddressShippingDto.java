package com.wilson.order.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressShippingDto {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
} 