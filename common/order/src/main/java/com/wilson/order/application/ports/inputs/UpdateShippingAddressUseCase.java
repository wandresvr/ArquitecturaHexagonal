package com.wilson.order.application.ports.inputs;

import com.wilson.order.domain.model.Order;
import com.wilson.order.domain.valueobjects.AddressShipping;

import java.util.UUID;

public interface UpdateShippingAddressUseCase {
    Order updateShippingAddress(UUID orderId, AddressShipping addressShipping);
} 