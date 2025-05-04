package com.itm.edu.order.application.ports.inputs;

import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.valueobjects.AddressShipping;

import java.util.UUID;

public interface UpdateShippingAddressUseCase {
    Order updateShippingAddress(UUID orderId, AddressShipping addressShipping);
} 