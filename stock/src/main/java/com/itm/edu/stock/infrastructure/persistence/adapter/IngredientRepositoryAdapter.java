package com.itm.edu.stock.infrastructure.persistence.adapter;

import com.itm.edu.stock.domain.entities.Ingredient;
import com.itm.edu.stock.domain.repository.IngredientRepository;
import com.itm.edu.stock.infrastructure.persistence.base.BaseRepositoryAdapter;
import com.itm.edu.stock.infrastructure.persistence.entity.IngredientJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.repository.IngredientJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IngredientRepositoryAdapter extends BaseRepositoryAdapter<IngredientJpaEntity, Ingredient> implements IngredientRepository {
    
    private final IngredientJpaRepository ingredientJpaRepository;

    public IngredientRepositoryAdapter(IngredientJpaRepository ingredientJpaRepository) {
        super(ingredientJpaRepository);
        this.ingredientJpaRepository = ingredientJpaRepository;
    }

    @Override
    protected IngredientJpaEntity toJpaEntity(Ingredient ingredient) {
        return IngredientJpaEntity.fromDomain(ingredient);
    }

    @Override
    public List<Ingredient> findBySupplier(String supplier) {
        return ingredientJpaRepository.findBySupplier(supplier).stream()
                .map(IngredientJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
} 