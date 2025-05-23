package com.itm.edu.order.infrastructure.persistence.repository;

import com.itm.edu.order.infrastructure.persistence.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {} 