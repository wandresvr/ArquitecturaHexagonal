package com.itm.edu.stock.infrastructure.persistence.mapper;

import com.itm.edu.stock.application.ports.output.UnitMapper;
import com.itm.edu.stock.application.ports.output.QuantityMapper;
import com.itm.edu.stock.infrastructure.persistence.valueobjects.UnitJpaValue;
import com.itm.edu.stock.infrastructure.persistence.valueobjects.QuantityJpaValue;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ValueObjectsMapper implements 
        UnitMapper<UnitJpaValue>,
        QuantityMapper<QuantityJpaValue> {
    
    @Override
    public UnitJpaValue toInfrastructure(String unit) {
        if (unit == null) return null;
        return new UnitJpaValue(unit);
    }

    @Override
    public String unitToDomain(UnitJpaValue jpaValue) {
        if (jpaValue == null) return null;
        return jpaValue.getValue();
    }

    @Override
    public QuantityJpaValue toInfrastructure(BigDecimal quantity) {
        if (quantity == null) return null;
        return new QuantityJpaValue(quantity);
    }

    @Override
    public BigDecimal quantityToDomain(QuantityJpaValue jpaValue) {
        if (jpaValue == null) return null;
        return jpaValue.getValue();
    }
} 