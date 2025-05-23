package com.itm.edu.order.application.ports.inputs;

import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.infrastructure.rest.dto.CreateClientDto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface CreateOrderUseCase {
    Order createOrder(CreateClientDto clientDto, Map<UUID, BigDecimal> productQuantities, AddressShipping addressShipping);
} 
