package com.itm.edu.order.application.dto;

import com.itm.edu.common.dto.ClientDTO;
import com.itm.edu.common.dto.AddressShippingDTO;
import com.itm.edu.common.dto.ProductOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private UUID orderId;
    private String orderStatus;
    private LocalDateTime orderDate;
    private AddressShippingDTO deliveryAddress;
    private BigDecimal total;
    private ClientDTO client;
    private List<ProductOrderDTO> products;
} 