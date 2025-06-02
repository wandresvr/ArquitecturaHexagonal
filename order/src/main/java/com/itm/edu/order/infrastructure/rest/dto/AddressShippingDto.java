package com.itm.edu.order.infrastructure.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressShippingDto {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
} 