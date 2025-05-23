package com.itm.edu.stock.infrastructure.persistence.repository;

import com.itm.edu.stock.infrastructure.persistence.base.BaseJpaRepository;
import com.itm.edu.stock.infrastructure.persistence.entity.RecipeJpaEntity;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeJpaRepository extends BaseJpaRepository<RecipeJpaEntity> {
    List<RecipeJpaEntity> findByDifficulty(String difficulty);
} 