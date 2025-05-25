package com.itm.edu.order.infrastructure.persistence.mapper;

import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.infrastructure.persistence.entities.ClientEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public ClientEntity toEntity(Client client) {
        if (client == null) return null;
        
        return ClientEntity.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .build();
    }

    public Client toDomain(ClientEntity entity) {
        if (entity == null) return null;
        
        // Aseguramos que los campos no sean nulos y limpiamos espacios
        String name = entity.getName() != null ? entity.getName().trim() : "";
        String email = entity.getEmail() != null ? entity.getEmail().trim() : "";
        String phone = entity.getPhone() != null ? entity.getPhone().trim() : "";
        
        // Asignamos valores por defecto si están vacíos
        name = name.isEmpty() ? "Cliente sin nombre" : name;
        email = email.isEmpty() ? "sin.email@dominio.com" : email;
        phone = phone.isEmpty() ? "0000000000" : phone;
        
        return Client.builder()
                .id(entity.getId())
                .name(name)
                .email(email)
                .phone(phone)
                .build();
    }
} 