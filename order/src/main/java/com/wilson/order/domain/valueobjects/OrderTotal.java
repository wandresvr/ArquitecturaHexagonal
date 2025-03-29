package com.wilson.order.domain.valueobjects;

public class OrderTotal {
    private Float subtotal;
    private Float tax;
    private Float shipping;
    private Float discount;
    private Float total;

    public OrderTotal(Float subtotal, Float tax, Float shipping, Float discount, Float total) {
        this.subtotal = subtotal;
        this.tax = tax;
        this.shipping = shipping;
        this.discount = discount;
        this.total = total;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public Float getShipping() {
        return shipping;
    }

    public void setShipping(Float shipping) {
        this.shipping = shipping;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

}
