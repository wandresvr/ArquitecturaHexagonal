package com.itm.edu.order.application.dto;

import com.itm.edu.common.dto.AddressShippingDTO;
import com.itm.edu.common.dto.ClientDTO;
import com.itm.edu.common.dto.ProductOrderDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderDTOTest {

    @Test
    void shouldCreateOrderDTOSuccessfully() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        String orderStatus = "PENDING";
        LocalDateTime orderDate = LocalDateTime.now();
        BigDecimal total = new BigDecimal("199.98");

        ClientDTO clientDTO = ClientDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        AddressShippingDTO addressDTO = AddressShippingDTO.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .build();

        ProductOrderDTO productOrderDTO = ProductOrderDTO.builder()
                .productId(UUID.randomUUID())
                .quantity(2)
                .build();

        // Act
        OrderDTO orderDTO = OrderDTO.builder()
                .orderId(orderId)
                .orderStatus(orderStatus)
                .orderDate(orderDate)
                .deliveryAddress(addressDTO)
                .total(total)
                .client(clientDTO)
                .products(Arrays.asList(productOrderDTO))
                .build();

        // Assert
        assertNotNull(orderDTO);
        assertEquals(orderId, orderDTO.getOrderId());
        assertEquals(orderStatus, orderDTO.getOrderStatus());
        assertEquals(orderDate, orderDTO.getOrderDate());
        assertEquals(addressDTO, orderDTO.getDeliveryAddress());
        assertEquals(total, orderDTO.getTotal());
        assertEquals(clientDTO, orderDTO.getClient());
        assertEquals(1, orderDTO.getProducts().size());
        assertEquals(productOrderDTO, orderDTO.getProducts().get(0));
    }

    @Test
    void shouldCreateEmptyOrderDTO() {
        // Act
        OrderDTO orderDTO = OrderDTO.builder().build();

        // Assert
        assertNotNull(orderDTO);
        assertNull(orderDTO.getOrderId());
        assertNull(orderDTO.getOrderStatus());
        assertNull(orderDTO.getOrderDate());
        assertNull(orderDTO.getDeliveryAddress());
        assertNull(orderDTO.getTotal());
        assertNull(orderDTO.getClient());
        assertNull(orderDTO.getProducts());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        UUID orderId = UUID.randomUUID();
        String orderStatus = "PENDING";
        LocalDateTime orderDate = LocalDateTime.now();
        BigDecimal total = new BigDecimal("199.98");
        ClientDTO clientDTO = ClientDTO.builder().build();
        AddressShippingDTO addressDTO = AddressShippingDTO.builder().build();
        ProductOrderDTO productOrderDTO = ProductOrderDTO.builder()
                .productId(UUID.randomUUID())
                .quantity(2)
                .build();

        // Act
        orderDTO.setOrderId(orderId);
        orderDTO.setOrderStatus(orderStatus);
        orderDTO.setOrderDate(orderDate);
        orderDTO.setDeliveryAddress(addressDTO);
        orderDTO.setTotal(total);
        orderDTO.setClient(clientDTO);
        orderDTO.setProducts(Arrays.asList(productOrderDTO));

        // Assert
        assertEquals(orderId, orderDTO.getOrderId());
        assertEquals(orderStatus, orderDTO.getOrderStatus());
        assertEquals(orderDate, orderDTO.getOrderDate());
        assertEquals(addressDTO, orderDTO.getDeliveryAddress());
        assertEquals(total, orderDTO.getTotal());
        assertEquals(clientDTO, orderDTO.getClient());
        assertEquals(1, orderDTO.getProducts().size());
        assertEquals(productOrderDTO, orderDTO.getProducts().get(0));
    }
} 