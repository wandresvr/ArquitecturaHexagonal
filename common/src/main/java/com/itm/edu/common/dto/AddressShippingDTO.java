package com.itm.edu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressShippingDTO {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
} 