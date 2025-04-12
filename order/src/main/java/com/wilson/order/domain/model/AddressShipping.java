package com.wilson.order.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressShipping {
    String street;
    String city;
    String state;
    String zipCode;
    String country;
} 