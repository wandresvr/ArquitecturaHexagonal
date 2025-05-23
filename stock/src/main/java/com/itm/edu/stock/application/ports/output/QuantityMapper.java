package com.itm.edu.stock.application.ports.output;

import java.math.BigDecimal;

public interface QuantityMapper<T> {
    T toInfrastructure(BigDecimal quantity);
    BigDecimal quantityToDomain(T infrastructureValue);
} 