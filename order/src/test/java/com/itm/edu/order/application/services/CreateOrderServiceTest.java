package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.application.ports.outputs.ClientRepositoryPort;
import com.itm.edu.order.application.ports.outputs.OrderPublisherPort;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.infrastructure.rest.dto.CreateClientDto;
import com.itm.edu.common.dto.OrderMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private ClientRepositoryPort clientRepository;

    @Mock
    private OrderPublisherPort orderPublisher;

    @InjectMocks
    private CreateOrderService createOrderService;

    private CreateClientDto clientDto;
    private AddressShipping addressShipping;
    private Product product;
    private UUID productId;
    private Map<UUID, BigDecimal> productQuantities;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("10.99"))
                .stock(100)
                .build();

        clientDto = new CreateClientDto();
        clientDto.setName("John Doe");
        clientDto.setEmail("john@example.com");
        clientDto.setPhone("1234567890");

        addressShipping = AddressShipping.builder()
                .street("123 Main St")
                .city("Test City")
                .state("Test State")
                .zipCode("12345")
                .country("Test Country")
                .build();

        productQuantities = new HashMap<>();
        productQuantities.put(productId, new BigDecimal("2"));
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Arrange
        Client savedClient = Client.builder()
                .id(UUID.randomUUID())
                .name(clientDto.getName())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .build();

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(orderPublisher).publish(any(OrderMessageDTO.class));

        // Act
        Order result = createOrderService.createOrder(clientDto, productQuantities, addressShipping);

        // Assert
        assertNotNull(result);
        assertEquals(savedClient, result.getClient());
        assertEquals(addressShipping, result.getDeliveryAddress());
        assertEquals(1, result.getProducts().size());
        
        verify(clientRepository).save(any(Client.class));
        verify(productRepository).findById(productId);
        verify(orderRepository).save(any(Order.class));
        verify(orderPublisher).publish(any(OrderMessageDTO.class));
    }

    @Test
    void shouldThrowExceptionWhenClientDtoIsNull() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createOrderService.createOrder(null, productQuantities, addressShipping));
        assertEquals("Los datos del cliente son requeridos", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProductQuantitiesIsNull() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createOrderService.createOrder(clientDto, null, addressShipping));
        assertEquals("Debe especificar al menos un producto", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProductQuantitiesIsEmpty() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createOrderService.createOrder(clientDto, new HashMap<>(), addressShipping));
        assertEquals("Debe especificar al menos un producto", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddressShippingIsNull() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createOrderService.createOrder(clientDto, productQuantities, null));
        assertEquals("La dirección de envío es requerida", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Arrange
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createOrderService.createOrder(clientDto, productQuantities, addressShipping));
        assertTrue(exception.getMessage().contains("Producto no encontrado con ID"));
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {
        // Arrange
        productQuantities.put(productId, BigDecimal.ZERO);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createOrderService.createOrder(clientDto, productQuantities, addressShipping));
        assertTrue(exception.getMessage().contains("La cantidad debe ser mayor a 0"));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientStock() {
        // Arrange
        productQuantities.put(productId, new BigDecimal("150")); // More than available stock
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createOrderService.createOrder(clientDto, productQuantities, addressShipping));
        assertTrue(exception.getMessage().contains("Stock insuficiente"));
    }

    @Test
    void shouldThrowExceptionWhenPublishingMessageFails() {
        // Arrange
        Client savedClient = Client.builder()
                .id(UUID.randomUUID())
                .name(clientDto.getName())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .build();

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doThrow(new RuntimeException("Error publishing message")).when(orderPublisher).publish(any(OrderMessageDTO.class));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> createOrderService.createOrder(clientDto, productQuantities, addressShipping));
        assertTrue(exception.getMessage().contains("Error al publicar el mensaje de la orden"));
    }
} 