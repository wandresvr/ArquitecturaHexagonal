package com.itm.edu.order.application.services;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.common.dto.ProductOrderDTO;
import com.itm.edu.order.application.ports.inputs.CreateOrderUseCase;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.repository.ClientRepository;
import com.itm.edu.order.domain.repository.OrderRepository;
import com.itm.edu.order.domain.repository.ProductRepository;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
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
        // 1) Save client first
        Client savedClient = clientRepository.save(client);
        
        // 2) Build entity with initial state
        Order order = Order.builder()
            .orderId(UUID.randomUUID())
            .orderDate(LocalDateTime.now())
            .client(savedClient)
            .deliveryAddress(shippingAddress)
            .orderStatus("PENDING_VALIDATION") // Estado inicial
            .build();

        productQuantities.forEach((pid, qty) -> {
            Product product = productRepository.findById(pid)
                .orElseThrow(() -> new RuntimeException("Product not found: " + pid));
            order.addProduct(product, qty);
        });

        // 3) Save the order locally
        Order savedOrder = orderRepository.save(order);

        // 4) Prepare and publish the OrderMessageDTO event
        OrderMessageDTO eventDto = OrderMessageDTO.builder()
                .orderId(savedOrder.getOrderId())
                .products(savedOrder.getProducts().stream()
                        .map(item -> ProductOrderDTO.builder() // Usar ProductOrderDTO del common dto
                                .productId(item.getProduct().getId())
                                .quantity(item.getQuantity().intValue()) // Asegurarse que common DTO usa int
                                .build())
                        .toList())
                .build();

        try {
            System.out.println("Publishing OrderMessageDTO to exchange: " + RabbitMQConfig.ORDER_EXCHANGE + ", key: " + RabbitMQConfig.ORDER_ROUTING_KEY + ", event: " + eventDto);
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_ROUTING_KEY, eventDto);
            System.out.println("Event published successfully.");
        } catch (Exception e) {
            System.err.println("ERROR publishing OrderMessageDTO: " + e.getMessage());
            // Considerar manejo de error/compensación
            // throw new RuntimeException("Failed to publish order creation event", e);
        }

        return savedOrder;   
    }
}
