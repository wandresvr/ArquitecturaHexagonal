package com.itm.edu.order.application.services;

import com.itm.edu.order.application.ports.inputs.CreateOrderUseCase;
import com.itm.edu.order.application.ports.outputs.OrderRepositoryPort;
import com.itm.edu.order.application.ports.outputs.ProductRepositoryPort;
import com.itm.edu.order.application.ports.outputs.ClientRepositoryPort;
import com.itm.edu.order.application.ports.outputs.OrderPublisherPort;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.infrastructure.rest.dto.CreateClientDto;
import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.common.dto.ProductOrderDTO;
import com.itm.edu.common.dto.ClientDTO;
import com.itm.edu.common.dto.AddressShippingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {
    private final OrderRepositoryPort orderRepository;
    private final ProductRepositoryPort productRepository;
    private final ClientRepositoryPort clientRepository;
    private final OrderPublisherPort orderPublisher;

    @Override
    @Transactional
    public Order createOrder(CreateClientDto clientDto, Map<UUID, BigDecimal> productQuantities, AddressShipping addressShipping) {
        validateOrderData(clientDto, productQuantities, addressShipping);
        
        // Crear y guardar el cliente
        Client client = createClient(clientDto);
        
        // Crear los items de la orden
        List<OrderItem> orderItems = createOrderItems(productQuantities);
        
        // Crear la orden usando el método de fábrica del dominio
        Order order = Order.create(client, orderItems, addressShipping);
        
        // Persistir la orden
        order = orderRepository.save(order);
        
        // Publicar el evento de orden creada
        publishOrderMessage(order);

        return order;
    }

    private Client createClient(CreateClientDto clientDto) {
        Client client = Client.builder()
            .id(UUID.randomUUID())
            .name(clientDto.getName())
            .email(clientDto.getEmail())
            .phone(clientDto.getPhone())
            .build();
            
        return clientRepository.save(client);
    }

    private void validateOrderData(CreateClientDto clientDto, Map<UUID, BigDecimal> productQuantities, AddressShipping addressShipping) {
        if (clientDto == null) {
            throw new BusinessException("Los datos del cliente son requeridos");
        }
        if (productQuantities == null || productQuantities.isEmpty()) {
            throw new BusinessException("Debe especificar al menos un producto");
        }
        if (addressShipping == null) {
            throw new BusinessException("La dirección de envío es requerida");
        }
    }

    private List<OrderItem> createOrderItems(Map<UUID, BigDecimal> productQuantities) {
        List<OrderItem> orderItems = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (Map.Entry<UUID, BigDecimal> entry : productQuantities.entrySet()) {
            try {
                Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new BusinessException("Producto no encontrado con ID: " + entry.getKey()));

                validateProductStock(product, entry.getValue());
                
                OrderItem orderItem = OrderItem.create(product, entry.getValue().intValue());
                orderItems.add(orderItem);
            } catch (IllegalArgumentException | BusinessException e) {
                errors.add(e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new BusinessException("Errores en la validación de productos: " + String.join(", ", errors));
        }

        return orderItems;
    }

    private void validateProductStock(Product product, BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("La cantidad debe ser mayor a 0 para el producto: " + product.getId());
        }
        
        if (product.getStock() < quantity.intValue()) {
            throw new BusinessException("Stock insuficiente para el producto: " + product.getName());
        }
    }

    private void publishOrderMessage(Order order) {
        try {
            OrderMessageDTO messageDTO = OrderMessageDTO.builder()
                .orderId(order.getOrderId())
                .client(mapClientToDTO(order.getClient()))
                .shippingAddress(mapAddressToDTO(order.getDeliveryAddress()))
                .products(mapOrderItemsToDTO(order.getProducts()))
                .build();

            orderPublisher.publish(messageDTO);
        } catch (Exception e) {
            throw new BusinessException("Error al publicar el mensaje de la orden: " + e.getMessage());
        }
    }

    private ClientDTO mapClientToDTO(Client client) {
        return ClientDTO.builder()
            .id(client.getId())
            .name(client.getName())
            .email(client.getEmail())
            .phone(client.getPhone())
            .build();
    }

    private AddressShippingDTO mapAddressToDTO(AddressShipping address) {
        return AddressShippingDTO.builder()
            .street(address.getStreet())
            .city(address.getCity())
            .state(address.getState())
            .zipCode(address.getZipCode())
            .country(address.getCountry())
            .build();
    }

    private List<ProductOrderDTO> mapOrderItemsToDTO(List<OrderItem> items) {
        return items.stream()
            .map(item -> ProductOrderDTO.builder()
                .productId(item.getProduct().getId())
                .quantity(item.getQuantity())
                .build())
            .collect(Collectors.toList());
    }
}
