package com.itm.edu.order.infrastructure.rest.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderRequestDtoTest {

    @Test
    void shouldCreateCreateOrderRequestDtoSuccessfully() {
        // Arrange
        CreateClientDto clientDto = CreateClientDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        UUID productId = UUID.randomUUID();
        Map<UUID, BigDecimal> productQuantities = new HashMap<>();
        productQuantities.put(productId, new BigDecimal("2"));

        AddressShippingDto addressShippingDto = AddressShippingDto.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        // Act
        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .client(clientDto)
                .products(Arrays.asList(
                        CreateOrderProductDto.builder()
                                .productId(productId)
                                .quantity(new BigDecimal("2"))
                                .build()
                ))
                .shippingAddress(addressShippingDto)
                .build();

        // Assert
        assertNotNull(requestDto);
        assertEquals(clientDto, requestDto.getClient());
        assertEquals(1, requestDto.getProducts().size());
        assertEquals(productId, requestDto.getProducts().get(0).getProductId());
        assertEquals(new BigDecimal("2"), requestDto.getProducts().get(0).getQuantity());
        assertEquals(addressShippingDto, requestDto.getShippingAddress());
    }

    @Test
    void shouldCreateEmptyCreateOrderRequestDto() {
        // Act
        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder().build();

        // Assert
        assertNotNull(requestDto);
        assertNull(requestDto.getClient());
        assertNull(requestDto.getProducts());
        assertNull(requestDto.getShippingAddress());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        // Arrange
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        CreateClientDto clientDto = CreateClientDto.builder().build();
        CreateOrderProductDto productDto = CreateOrderProductDto.builder()
                .productId(UUID.randomUUID())
                .quantity(new BigDecimal("1"))
                .build();
        AddressShippingDto addressShippingDto = AddressShippingDto.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        // Act
        requestDto.setClient(clientDto);
        requestDto.setProducts(Arrays.asList(productDto));
        requestDto.setShippingAddress(addressShippingDto);

        // Assert
        assertEquals(clientDto, requestDto.getClient());
        assertEquals(1, requestDto.getProducts().size());
        assertEquals(productDto, requestDto.getProducts().get(0));
        assertEquals(addressShippingDto, requestDto.getShippingAddress());
    }
} 