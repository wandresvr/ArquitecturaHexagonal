package com.itm.edu.stock.infrastructure.persistence.base;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class BaseRepositoryAdapter<T extends BaseJpaEntity<D>, D> {
    protected final BaseJpaRepository<T, D> repository;

    protected BaseRepositoryAdapter(BaseJpaRepository<T, D> repository) {
        this.repository = repository;
    }

    protected abstract T toJpaEntity(D domain);

    public D save(D domain) {
        T entity = toJpaEntity(domain);
        return repository.save(entity).toDomain();
    }

    public Optional<D> findById(UUID id) {
        return repository.findById(id).map(BaseJpaEntity::toDomain);
    }

    public List<D> findAll() {
        return repository.findAll().stream()
                .map(BaseJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
} 