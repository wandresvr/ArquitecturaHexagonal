package com.itm.edu.stock.domain.valueobjects;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Quantity {
    @Column(name = "quantity")
    private BigDecimal value;

    public boolean isLessThan(BigDecimal other) {
        return this.value.compareTo(other) < 0;
    }

    public Quantity subtract(BigDecimal amount) {
        return new Quantity(this.value.subtract(amount));
    }

    public BigDecimal getValue() {
        return value;
    }
}
