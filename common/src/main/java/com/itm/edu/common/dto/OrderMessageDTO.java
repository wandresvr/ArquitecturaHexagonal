package com.itm.edu.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
public class OrderMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private UUID orderId;
    private ClientDTO client;
    private AddressShippingDTO shippingAddress;
    private List<ProductOrderDTO> products;
} 