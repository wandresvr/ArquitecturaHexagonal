package com.itm.edu.stock.application.ports.output;

public interface UnitMapper<T> {
    T toInfrastructure(String unit);
    String unitToDomain(T infrastructureValue);
} 