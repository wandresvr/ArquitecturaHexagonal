package com.wilson.order.domain.model;
import com.wilson.order.domain.valueobjects.OrderTotal;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private  String orderId;
    private List<Product> productsOrder = new ArrayList<>();
    private String customerId;
    private String orderStatus;
    private String orderDate;
    private String deliveryDate;
    private String deliveryAddress;
    private String paymentMethod;
    private String paymentStatus;
    private OrderTotal orderTotal;
    private String orderNotes;


    public Order(String orderId, List<Product> productsOrder, String customerId, String orderStatus, String orderDate,
            String deliveryDate, String deliveryAddress, String paymentMethod, String paymentStatus, OrderTotal orderTotal,
            String orderDiscount, String orderTax, String orderShipping, String orderTrackingNumber,
            String orderTrackingUrl, String orderNotes, String orderCreatedBy) {
        this.orderId = orderId;
        this.productsOrder = productsOrder;
        this.customerId = customerId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.orderTotal = orderTotal;
        this.orderNotes = orderNotes;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public List<Product> addProductsOrder(Product product) {
        this.productsOrder.add(product);
        return this.productsOrder;
    }
    public void removeProductsOrder(Product product) {
        this.productsOrder.remove(product);
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public String getDeliveryDate() {
        return deliveryDate;
    }
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderNotes() {
        return orderNotes;
    }
    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }


}
