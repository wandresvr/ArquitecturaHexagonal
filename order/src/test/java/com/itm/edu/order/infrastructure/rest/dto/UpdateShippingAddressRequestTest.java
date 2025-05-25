package com.itm.edu.order.infrastructure.rest.dto;

import com.itm.edu.order.domain.valueobjects.AddressShipping;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UpdateShippingAddressRequestTest {

    @Test
    void shouldCreateUpdateShippingAddressRequestSuccessfully() {
        // Arrange
        AddressShipping addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        // Act
        UpdateShippingAddressRequest request = UpdateShippingAddressRequest.builder()
                .addressShipping(addressShipping)
                .build();

        // Assert
        assertNotNull(request);
        assertEquals(addressShipping, request.getAddressShipping());
    }

    @Test
    void shouldCreateEmptyUpdateShippingAddressRequest() {
        // Act
        UpdateShippingAddressRequest request = UpdateShippingAddressRequest.builder().build();

        // Assert
        assertNotNull(request);
        assertNull(request.getAddressShipping());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        // Arrange
        UpdateShippingAddressRequest request = new UpdateShippingAddressRequest();
        AddressShipping addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        // Act
        request.setAddressShipping(addressShipping);

        // Assert
        assertEquals(addressShipping, request.getAddressShipping());
    }
} 