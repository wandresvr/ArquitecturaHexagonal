package com.itm.edu.order.application.ports.outputs;

import com.itm.edu.order.domain.model.Client;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepositoryPort {
    Client save(Client client);
    Optional<Client> findById(UUID id);
    Optional<Client> findByEmail(String email);
} 