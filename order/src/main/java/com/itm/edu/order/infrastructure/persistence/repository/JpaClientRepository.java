package com.itm.edu.order.infrastructure.persistence.repository;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.repository.ClientRepository;
import com.itm.edu.order.infrastructure.persistence.entities.ClientEntity;
import com.itm.edu.order.infrastructure.persistence.mapper.ClientMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaClientRepository implements ClientRepository {
    private final SpringJpaClientRepository jpaRepository;
    private final ClientMapper clientMapper;

    public JpaClientRepository(SpringJpaClientRepository jpaRepository, ClientMapper clientMapper) {
        this.jpaRepository = jpaRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public Client save(Client client) {
        ClientEntity entity = clientMapper.toEntity(client);
        ClientEntity savedEntity = jpaRepository.save(entity);
        return clientMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(clientMapper::toDomain);
    }

    @Override
    public List<Client> findAll() {
        return jpaRepository.findAll().stream()
                .map(clientMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}

interface SpringJpaClientRepository extends JpaRepository<ClientEntity, UUID> {} 