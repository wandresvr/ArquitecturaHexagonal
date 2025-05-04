package com.itm.edu.order.application.dto;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private UUID orderId;
    private String orderStatus;
    private LocalDateTime orderDate;
    private AddressShipping deliveryAddress;
    private OrderTotalValue total;
    private ClientDTO client;
    private List<OrderItemDTO> products;
} 