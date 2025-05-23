package com.itm.edu.stock.infrastructure.persistence.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.UUID;

@NoRepositoryBean
public interface BaseJpaRepository<T extends BaseJpaEntity<D>, D> extends JpaRepository<T, UUID> {
} 