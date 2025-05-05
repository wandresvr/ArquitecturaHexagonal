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
        
        // 2) Build and save entity
        Order order = Order.builder()
            .orderId(UUID.randomUUID())
            .orderDate(LocalDateTime.now())
            .client(savedClient)
            .deliveryAddress(shippingAddress)
            .build();

        productQuantities.forEach((pid, qty) -> {
            Product product = productRepository.findById(pid)
                .orElseThrow(() -> new RuntimeException("Product not found: " + pid));
            order.addProduct(product, qty);
        });

        Order saved = orderRepository.save(order);

        // 3) Map to DTO
        OrderMessageDTO dto = OrderMessageDTO.builder()
            .orderId(saved.getOrderId())
            .products(saved.getProducts().stream()
                .<ProductOrderDTO>map(line -> ProductOrderDTO.builder()
                    .productId(line.getProduct().getId())
                    .quantity(line.getQuantity().intValue())
                    .build())
                .toList())
            .build();

        // Verificar el tipo de convertidor y el mensaje
        System.out.println("Tipo de MessageConverter: " + rabbitTemplate.getMessageConverter().getClass().getName());
        System.out.println("Mensaje a enviar: " + dto);
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.ORDER_EXCHANGE,
            RabbitMQConfig.ORDER_ROUTING_KEY,
            dto
        );

        return saved;
    }
}
