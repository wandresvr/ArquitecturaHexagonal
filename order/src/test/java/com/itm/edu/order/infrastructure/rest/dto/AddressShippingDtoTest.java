package com.itm.edu.order.infrastructure.rest.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressShippingDtoTest {

    @Test
    void shouldCreateAddressShippingDtoSuccessfully() {
        // Arrange
        String street = "123 Main St";
        String city = "New York";
        String state = "NY";
        String zipCode = "10001";
        String country = "USA";

        // Act
        AddressShippingDto addressDto = AddressShippingDto.builder()
                .street(street)
                .city(city)
                .state(state)
                .zipCode(zipCode)
                .country(country)
                .build();

        // Assert
        assertNotNull(addressDto);
        assertEquals(street, addressDto.getStreet());
        assertEquals(city, addressDto.getCity());
        assertEquals(state, addressDto.getState());
        assertEquals(zipCode, addressDto.getZipCode());
        assertEquals(country, addressDto.getCountry());
    }

    @Test
    void shouldCreateEmptyAddressShippingDto() {
        // Act
        AddressShippingDto addressDto = AddressShippingDto.builder().build();

        // Assert
        assertNotNull(addressDto);
        assertNull(addressDto.getStreet());
        assertNull(addressDto.getCity());
        assertNull(addressDto.getState());
        assertNull(addressDto.getZipCode());
        assertNull(addressDto.getCountry());
    }
} 