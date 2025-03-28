package com.wilson.order.domain.model;

import java.util.List;

public class Order {

    private  String orderId;
    private List<String> productsOrder;
    private String customerId;
    private String orderStatus;
    private String orderDate;
    private String deliveryDate;
    private String deliveryAddress;
    private String paymentMethod;
    private String paymentStatus;
    private String orderTotal;
    private String orderDiscount;
    private String orderShipping;
    private String orderTrackingNumber;
    private String orderNotes;
    private String orderCreatedBy;
    private String orderUpdatedBy;
    private String orderCreatedAt;
    private String orderUpdatedAt;
    private String orderCancelledAt;
    private String orderCancelledBy;
    private String orderCancelledReason;

    public class InnerOrder {
        private String orderId;
        private String orderStatus;
        private String orderDate;
        private String deliveryDate;
        private String deliveryAddress;
        private String paymentMethod;
        private String paymentStatus;
        private String orderTotal;
        private String orderDiscount;
        private String orderTax;
        private String orderShipping;
        private String orderTrackingNumber;
        private String orderTrackingUrl;
        private String orderNotes;
        private String orderCreatedBy;
    
        
    }

    public Order(String orderId, List<String> productsOrder, String customerId, String orderStatus, String orderDate,
            String deliveryDate, String deliveryAddress, String paymentMethod, String paymentStatus, String orderTotal,
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
        this.orderDiscount = orderDiscount;
        this.orderShipping = orderShipping;
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderNotes = orderNotes;
        this.orderCreatedBy = orderCreatedBy;

    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public List<String> getProductsOrder() {
        return productsOrder;
    }
    public void setProductsOrder(List<String> productsOrder) {
        this.productsOrder = productsOrder;
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
    public String getOrderTotal() {
        return orderTotal;
    }
    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }


}
