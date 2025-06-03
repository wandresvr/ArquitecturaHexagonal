package com.itm.edu.order.infrastructure.persistence;

import com.itm.edu.order.application.ports.outputs.ClientRepositoryPort;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.infrastructure.persistence.repository.JpaClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientRepositoryAdapter implements ClientRepositoryPort {
    private final JpaClientRepository clientRepository;

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return clientRepository.findById(id);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
} 