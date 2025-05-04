package com.itm.edu.order.application.services;

import com.itm.edu.common.dto.OrderMessageDTO;
import com.itm.edu.common.dto.ProductOrderDTO;
import com.itm.edu.order.application.ports.inputs.CreateOrderUseCase;
import com.itm.edu.order.application.ports.outputs.OrderPublisherPort;
import com.itm.edu.order.domain.exception.ProductNotFoundException;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.repository.OrderRepository;
import com.itm.edu.order.domain.repository.ProductRepository;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderPublisherPort orderPublisher;  // ← el puerto

    @Override
    @Transactional
    public Order createOrder(Client client,
                             Map<UUID, BigDecimal> productQuantities,
                             AddressShipping shippingAddress) {

        // 1) Construir y guardar la orden
        Order order = Order.builder()
                .orderId(UUID.randomUUID())
                .orderDate(java.time.LocalDateTime.now())
                .deliveryAddress(shippingAddress)
                .client(client)
                .build();

        productQuantities.forEach((productId, quantity) -> {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));
            order.addProduct(product, quantity);
        });

        Order saved = orderRepository.save(order);

        // 2) Mapear a DTO (common-dto)
        OrderMessageDTO message = OrderMessageDTO.builder()
            .orderId(saved.getOrderId())
            .products(saved.getProducts().stream()
                .<ProductOrderDTO>map(line -> ProductOrderDTO.builder()
                    .productId(line.getProduct().getId())
                    .quantity(line.getQuantity().intValue())
                    .build())
                .toList())
            .build();

        // 3) Publicar usando el puerto
        orderPublisher.publish(message);

        return saved;
    }
}
