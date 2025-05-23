package com.itm.edu.stock.infrastructure.persistence.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseJpaEntity<T> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    protected UUID id;

    public abstract T toDomain();
} 