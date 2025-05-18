package com.itm.edu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressShippingDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
} 