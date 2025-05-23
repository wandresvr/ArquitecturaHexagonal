package com.itm.edu.order.application.mapper;

public interface DomainMapper<D, E> {
    D toDomain(E entity);
    E toEntity(D domain);
} 