package com.itm.edu.order.application.services;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.common.dto.ProductOrderDTO;
import com.itm.edu.common.dto.ClientDTO;
import com.itm.edu.common.dto.AddressShippingDTO;
import com.itm.edu.order.application.ports.inputs.CreateOrderUseCase;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.repository.ClientRepository;
import com.itm.edu.order.domain.repository.OrderRepository;
import com.itm.edu.order.domain.repository.ProductRepository;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.exception.BusinessException;
import com.itm.edu.order.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Order createOrder(Client client,
                             Map<UUID, BigDecimal> productQuantities,
                             AddressShipping shippingAddress) {
        try {
            // 1) Validar el cliente
            if (client == null || client.getName() == null || client.getEmail() == null) {
                throw new BusinessException("La información del cliente es requerida");
            }

            // 2) Validar productos
            if (productQuantities == null || productQuantities.isEmpty()) {
                throw new BusinessException("Se requiere al menos un producto");
            }

            // 3) Validar dirección de envío
            if (shippingAddress == null || shippingAddress.getStreet() == null) {
                throw new BusinessException("La dirección de envío es requerida");
            }

            // 4) Guardar el cliente
            Client savedClient = clientRepository.save(client);
            
            // 5) Crear la orden
            Order order = Order.builder()
                .client(savedClient)
                .orderStatus("PENDING_VALIDATION")
                .orderDate(LocalDateTime.now())
                .deliveryAddress(shippingAddress)
                .build();

            // 6) Agregar productos y validar su existencia
            for (Map.Entry<UUID, BigDecimal> entry : productQuantities.entrySet()) {
                UUID productId = entry.getKey();
                BigDecimal quantity = entry.getValue();

                if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusinessException("La cantidad del producto debe ser mayor a 0");
                }

                Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new BusinessException("Producto no encontrado: " + productId));

                order.addProduct(product, quantity);
            }

            // 7) Guardar la orden
            Order savedOrder = orderRepository.save(order);

            // 8) Publicar mensaje en RabbitMQ
            OrderMessageDTO orderMessage = OrderMessageDTO.builder()
                .orderId(savedOrder.getOrderId())
                .client(ClientDTO.builder()
                    .id(savedOrder.getClient().getId())
                    .name(savedOrder.getClient().getName())
                    .email(savedOrder.getClient().getEmail())
                    .phone(savedOrder.getClient().getPhone())
                    .build())
                .shippingAddress(AddressShippingDTO.builder()
                    .street(savedOrder.getDeliveryAddress().getStreet())
                    .city(savedOrder.getDeliveryAddress().getCity())
                    .state(savedOrder.getDeliveryAddress().getState())
                    .zipCode(savedOrder.getDeliveryAddress().getZipCode())
                    .country(savedOrder.getDeliveryAddress().getCountry())
                    .build())
                .products(savedOrder.getProducts().stream()
                    .map(item -> ProductOrderDTO.builder()
                        .productId(item.getProduct().getId())
                        .quantity(item.getQuantity().intValue())
                        .build())
                    .collect(Collectors.toList()))
                .build();

            rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                orderMessage
            );

            return savedOrder;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Error al crear la orden: " + e.getMessage());
        }
    }
}
