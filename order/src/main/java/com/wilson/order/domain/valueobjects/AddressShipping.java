package com.wilson.order.domain.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressShipping {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
