package com.itm.edu.order.infrastructure.persistence;

import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            entityManager.persist(product);
        } else {
            entityManager.merge(product);
        }
        return product;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Product.class, id));
    }

    @Override
    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }
} 