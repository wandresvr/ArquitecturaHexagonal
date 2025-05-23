package com.itm.edu.stock.infrastructure.persistence.adapter;

import com.itm.edu.stock.domain.entities.Recipe;
import com.itm.edu.stock.domain.repository.RecipeRepository;
import com.itm.edu.stock.infrastructure.persistence.base.BaseRepositoryAdapter;
import com.itm.edu.stock.infrastructure.persistence.entity.RecipeJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.repository.RecipeJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeRepositoryAdapter extends BaseRepositoryAdapter<RecipeJpaEntity, Recipe> implements RecipeRepository {
    
    private final RecipeJpaRepository recipeJpaRepository;

    public RecipeRepositoryAdapter(RecipeJpaRepository recipeJpaRepository) {
        super(recipeJpaRepository);
        this.recipeJpaRepository = recipeJpaRepository;
    }

    @Override
    protected RecipeJpaEntity toJpaEntity(Recipe recipe) {
        return RecipeJpaEntity.fromDomain(recipe);
    }

    @Override
    public List<Recipe> findByDifficulty(String difficulty) {
        return recipeJpaRepository.findByDifficulty(difficulty).stream()
                .map(RecipeJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
} 