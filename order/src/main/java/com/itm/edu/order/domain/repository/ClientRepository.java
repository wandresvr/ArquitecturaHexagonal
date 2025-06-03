package com.itm.edu.order.domain.repository;

import com.itm.edu.order.domain.model.Client;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(UUID id);
    List<Client> findAll();
    void deleteById(UUID id);
    Optional<Client> findByEmail(String email);
} 