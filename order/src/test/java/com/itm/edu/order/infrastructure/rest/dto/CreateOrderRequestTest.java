package com.itm.edu.order.infrastructure.rest.dto;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderRequestTest {

    @Test
    void shouldCreateCreateOrderRequestSuccessfully() {
        // Arrange
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(UUID.randomUUID(), new BigDecimal("2"));

        AddressShipping addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        // Act
        CreateOrderRequest request = CreateOrderRequest.builder()
                .client(clientDto)
                .productQuantities(productQuantities)
                .addressShipping(addressShipping)
                .build();

        // Assert
        assertNotNull(request);
        assertEquals(clientDto, request.getClient());
        assertEquals(productQuantities, request.getProductQuantities());
        assertEquals(addressShipping, request.getAddressShipping());
    }

    @Test
    void shouldCreateEmptyCreateOrderRequest() {
        // Act
        CreateOrderRequest request = CreateOrderRequest.builder().build();

        // Assert
        assertNotNull(request);
        assertNull(request.getClient());
        assertNull(request.getProductQuantities());
        assertNull(request.getAddressShipping());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest();
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(UUID.randomUUID(), new BigDecimal("2"));

        AddressShipping addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        // Act
        request.setClient(clientDto);
        request.setProductQuantities(productQuantities);
        request.setAddressShipping(addressShipping);

        // Assert
        assertEquals(clientDto, request.getClient());
        assertEquals(productQuantities, request.getProductQuantities());
        assertEquals(addressShipping, request.getAddressShipping());
    }
} 