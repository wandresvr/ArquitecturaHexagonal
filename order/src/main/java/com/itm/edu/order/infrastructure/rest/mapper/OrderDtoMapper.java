package com.itm.edu.order.infrastructure.rest.mapper;

import com.itm.edu.order.domain.model.Order;
import com.itm.edu.order.domain.model.Client;
import com.itm.edu.order.domain.model.OrderItem;
import com.itm.edu.order.domain.model.Product;
import com.itm.edu.order.domain.valueobjects.AddressShipping;
import com.itm.edu.order.domain.valueobjects.OrderTotalValue;
import com.itm.edu.order.infrastructure.rest.dto.OrderDto;
import com.itm.edu.order.infrastructure.rest.dto.OrderItemResponseDto;
import com.itm.edu.order.infrastructure.rest.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderDtoMapper {
    
    private final ProductDtoMapper productDtoMapper;

    public OrderDtoMapper(ProductDtoMapper productDtoMapper) {
        this.productDtoMapper = productDtoMapper;
    }
    
    public OrderDto toDto(Order domain) {
        if (domain == null) return null;
        
        return OrderDto.builder()
                .orderId(domain.getOrderId())
                .client(toClientDto(domain.getClient()))
                .items(domain.getProducts().stream()
                        .map(this::toOrderItemDto)
                        .collect(Collectors.toList()))
                .shippingAddress(toAddressDto(domain.getDeliveryAddress()))
                .total(toMoneyDto(domain.getTotal()))
                .orderDate(domain.getOrderDate())
                .orderStatus(domain.getOrderStatus())
                .build();
    }

    private OrderDto.ClientDto toClientDto(Client domain) {
        if (domain == null) return null;
        
        return OrderDto.ClientDto.builder()
                .id(domain.getId())
                .name(domain.getName())
                .email(domain.getEmail())
                .phone(domain.getPhone())
                .build();
    }

    private OrderDto.OrderItemDto toOrderItemDto(OrderItem domain) {
        if (domain == null) return null;
        
        return OrderDto.OrderItemDto.builder()
                .product(toProductDto(domain.getProduct()))
                .quantity(domain.getQuantity())
                .build();
    }

    private OrderDto.ProductDto toProductDto(Product domain) {
        if (domain == null) return null;
        
        return OrderDto.ProductDto.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .stock(domain.getStock())
                .build();
    }

    private OrderDto.AddressDto toAddressDto(AddressShipping domain) {
        if (domain == null) return null;
        
        return OrderDto.AddressDto.builder()
                .street(domain.getStreet())
                .city(domain.getCity())
                .state(domain.getState())
                .zipCode(domain.getZipCode())
                .country(domain.getCountry())
                .build();
    }

    private OrderDto.MoneyDto toMoneyDto(OrderTotalValue domain) {
        if (domain == null) return null;
        
        return OrderDto.MoneyDto.builder()
                .amount(domain.getAmount())
                .currency(domain.getCurrency())
                .build();
    }
} 