package com.itm.edu.order.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Client {
    private final UUID id;
    private final String name;
    private final String email;
    private final String phone;

    public Client(UUID id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        validate();
    }

    private void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email del cliente no puede estar vacío");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono del cliente no puede estar vacío");
        }
    }
}
