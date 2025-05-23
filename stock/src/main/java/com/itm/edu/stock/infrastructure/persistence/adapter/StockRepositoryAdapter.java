package com.itm.edu.stock.infrastructure.persistence.adapter;

import com.itm.edu.stock.domain.entities.Stock;
import com.itm.edu.stock.domain.repository.StockRepository;
import com.itm.edu.stock.infrastructure.persistence.base.BaseRepositoryAdapter;
import com.itm.edu.stock.infrastructure.persistence.entity.StockJpaEntity;
import com.itm.edu.stock.infrastructure.persistence.repository.StockJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockRepositoryAdapter extends BaseRepositoryAdapter<StockJpaEntity, Stock> implements StockRepository {
    
    private final StockJpaRepository stockJpaRepository;

    public StockRepositoryAdapter(StockJpaRepository stockJpaRepository) {
        super(stockJpaRepository);
        this.stockJpaRepository = stockJpaRepository;
    }

    @Override
    protected StockJpaEntity toJpaEntity(Stock stock) {
        return StockJpaEntity.fromDomain(stock);
    }

    @Override
    public List<Stock> findByIngredientId(UUID ingredientId) {
        return stockJpaRepository.findByIngredientId(ingredientId).stream()
                .map(StockJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
} 